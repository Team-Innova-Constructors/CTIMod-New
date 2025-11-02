package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import static com.c2h6s.etshtinker.util.vecCalc.getMold;

public class DragonsWifu extends EtSTBaseModifier {

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        Entity entity = context.getTarget();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.invulnerableTime = 0;
            livingEntity.hurt(DamageSource.indirectMagic(context.getAttacker(), context.getAttacker()), damage * 0.25f * modifier.getLevel());
            livingEntity.invulnerableTime = 0;
            livingEntity.hurt(DamageSource.DRAGON_BREATH, damage * 0.25f * modifier.getLevel());
            livingEntity.invulnerableTime = 0;
        }
        return knockback;
    }

    @Override
    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target != null && projectile instanceof AbstractArrow arrow) {
            target.invulnerableTime = 0;
            target.hurt(DamageSource.DRAGON_BREATH, (float) (arrow.getBaseDamage() * getMold(arrow.getDeltaMovement()) * 0.25f * modifier.getLevel()));
            target.invulnerableTime = 0;
        }
        return false;
    }
}
