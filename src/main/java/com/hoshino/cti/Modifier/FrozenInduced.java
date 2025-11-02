package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.content.environmentSystem.EnvironmentalHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import static com.hoshino.cti.content.environmentSystem.EnvironmentalHandler.*;

public class FrozenInduced extends EtSTBaseModifier {

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        Entity entity = context.getTarget();
        LivingEntity living = context.getAttacker();
        if (entity instanceof LivingEntity target && living instanceof Player player && !(target instanceof Player)) {
            target.invulnerableTime = 0;
            target.hurt(EDamageSource.indirectIonize(false, player,modifier.getLevel()), damageDealt / 4);
            if (getFrozenResistance(target) <= 1.5 && getFrozenValue(target) < 1000) {
                addFrozenValue(target, 10 * modifier.getLevel());
            }
            target.invulnerableTime = 0;
        }
    }

    @Override
    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target != null && projectile instanceof AbstractArrow arrow && attacker instanceof Player player && !(target instanceof Player)) {
            target.invulnerableTime = 0;
            target.hurt(EDamageSource.indirectIonize(false, player,modifier.getLevel()), (float) arrow.getBaseDamage() / 4);
            if (getFrozenResistance(target) <= 1.5 && getFrozenValue(target) < 1000) {
                addFrozenValue(target, 10 * modifier.getLevel());
            }
            target.invulnerableTime = 0;
        }
        return false;
    }
}
