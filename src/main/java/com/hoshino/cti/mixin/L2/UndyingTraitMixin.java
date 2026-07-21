package com.hoshino.cti.mixin.L2;

import com.gjhi.tinkersinnovation.register.TinkersInnovationModifiers;
import com.hoshino.cti.content.environmentSystem.IEnvironmentalSource;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.SearchTools;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.content.traits.legendary.UndyingTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import slimeknights.tconstruct.library.modifiers.ModifierId;

@Mixin(value = UndyingTrait.class, remap = false)
public abstract class UndyingTraitMixin extends LegendaryTrait {
    @Unique
    private String cti_new$DEATH = "undying_has_dead";

    public UndyingTraitMixin(ChatFormatting format) {
        super(format);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite()
    public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
        if (!entity.getLevel().isClientSide()) {
            if(!cti_new$shouldRespawn(entity,event.getSource()))return;
            entity.setHealth(entity.getMaxHealth());
            if (entity.isAlive()) {
                event.setCanceled(true);
                L2Hostility.HANDLER.toTrackingPlayers(new TraitEffectToClient(entity, this, TraitEffects.UNDYING), entity);
            }
        }
    }

    @Unique
    private boolean cti_new$shouldRespawn(LivingEntity living, DamageSource source) {
        MobTraitCap traitCap = MobTraitCap.HOLDER.get(living);
        if (!this.validTarget(living)) return false;
        if (traitCap.hasTrait(LHTraits.SPLIT.get())) return false;
        if (source.isBypassInvul()) return false;
        if (source instanceof IEnvironmentalSource) return false;
        if (living.getPersistentData().contains("atomic_dec") || living.getPersistentData().contains("quark_disassemble")) return false;
        var attacker=source.getEntity();
        if (attacker instanceof Player player) {
            if (GetModifierLevel.CurioHasModifierlevel(player, new ModifierId("solidarytinker:bha"))) {
                return false;
            }
        }
        boolean hasDead = cti_new$hadRespawned(living);
        if (!hasDead) {
            cti_new$writeNbt(living);
            return true;
        }
        if (living.hasEffect(SearchTools.findMobEffect("solidarytinker:healhysteresis"))) return false;
        if(attacker instanceof Player player){
            if (GetModifierLevel.getEachHandsTotalModifierlevel(player, TinkersInnovationModifiers.L2ComplementsModifier.curse_blade.getId()) > 0 || GetModifierLevel.getEachHandsTotalModifierlevel(player, CtiModifiers.CURSED_ARROW.getId()) > 0) {
                return false;
            }
        }
        return !living.hasEffect(LCEffects.CURSE.get());
    }

    @Unique
    private boolean cti_new$hadRespawned(LivingEntity living) {
        return living.getPersistentData().contains(cti_new$DEATH);
    }

    @Unique
    private void cti_new$writeNbt(LivingEntity living) {
        living.getPersistentData().putBoolean(cti_new$DEATH, true);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean validTarget(LivingEntity le) {
        return !(le instanceof EnderDragon);
    }
}
