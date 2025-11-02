package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.Projectiles.StarDargonAmmo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StarDargonHit extends Modifier implements MeleeHitModifierHook , MeleeDamageModifierHook , TooltipModifierHook , ProjectileHitModifierHook {
    public static final ResourceLocation STAR_DUST = Cti.getResource("star_dust");
    @Override
    public int getPriority() {
        return 10;
    }
    public static final Map<UUID, Float> DAMAGE_SHOULD_BE = new ConcurrentHashMap<>();
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT,ModifierHooks.MELEE_DAMAGE,ModifierHooks.TOOLTIP,ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var player=context.getPlayerAttacker();
        var target=context.getLivingTarget();
        if(player==null||target==null)return;
        if(target.getHealth()/target.getMaxHealth()<0.18f){
            target.die(DamageSource.playerAttack(player));
            target.discard();
            tool.getPersistentData().putInt(STAR_DUST,tool.getPersistentData().getInt(STAR_DUST)+1);
            player.getLevel().playSound(null,player,SoundEvents.ENDER_DRAGON_AMBIENT, SoundSource.AMBIENT,1f,1f);
        }

    }
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var player=context.getPlayerAttacker();
        if(player!=null){
            DAMAGE_SHOULD_BE.put(player.getUUID(),damage+500);
        }
        return damage+500;
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.literal("当前星尘数量"+getDustAmount(iToolStackView)).withStyle(style -> style.withColor(0x3845ff)));
    }
    private int getDustAmount(IToolStackView view){
        return view.getPersistentData().getInt(STAR_DUST);
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if(attacker instanceof Player player&&target!=null){
            if(persistentData.getInt(STAR_DUST)>10){
                persistentData.putInt(STAR_DUST,persistentData.getInt(STAR_DUST)-10);
                float damageShouldBe=DAMAGE_SHOULD_BE.getOrDefault(player.getUUID(),10f);
                var ammo=new StarDargonAmmo(player,player.getLevel(),target.blockPosition(),damageShouldBe);
                player.getLevel().addFreshEntity(ammo);
            }
        }
        return false;
    }
}
