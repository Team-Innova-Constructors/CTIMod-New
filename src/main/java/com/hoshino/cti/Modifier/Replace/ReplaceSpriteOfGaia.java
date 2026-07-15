package com.hoshino.cti.Modifier.Replace;

import com.xiaoyue.tinkers_ingenuity.utils.LoadingUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import vazkii.botania.api.mana.ManaItemHandler;

public class ReplaceSpriteOfGaia extends Modifier implements MeleeHitModifierHook, BreakSpeedModifierHook, ProjectileHitModifierHook {

    @Override
    public void onBreakSpeed(IToolStackView iToolStackView, ModifierEntry modifierEntry, PlayerEvent.BreakSpeed breakSpeed, Direction direction, boolean b, float v) {
        Entity entity = breakSpeed.getEntity();
        if (LoadingUtils.isLoadBot() && entity instanceof Player player) {
            if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, 160, true)) {
                breakSpeed.setNewSpeed(breakSpeed.getNewSpeed() * 1.5F);
            }
        }
    }
    private EntityDamageSource wither(LivingEntity living){
        var s= new EntityDamageSource(DamageSource.WITHER.msgId,living);
        s.bypassArmor();
        return s;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (LoadingUtils.isLoadBot() && attacker instanceof Player player&&projectile instanceof AbstractArrow arrow&&target!=null) {
            if (ManaItemHandler.instance().requestManaExact(attacker.getMainHandItem(), player, 160, true)) {
                ToolAttackUtil.attackEntitySecondary(wither(player),(float) arrow.getBaseDamage() * 0.3f * modifier.getLevel(), target, player, false);
            }
        }
        return false;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (LoadingUtils.isLoadBot() && context.getAttacker() instanceof Player player) {
            var target=context.getLivingTarget();
            if(target==null)return;
            if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, 160, true)) {
                ToolAttackUtil.attackEntitySecondary(wither(player),damageDealt * 0.3f * modifier.getLevel(), target, player, false);
            }
        }
    }
}
