package com.hoshino.cti.Modifier.aetherCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.ManaBurstEntity;

public class GravityShield extends EtSTBaseModifier {
    @Override
    public float modifierDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.isProjectile()) return amount*0.25f;
        else return amount*0.75f;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide&&holder!=null){
            AABB aabb = holder.getBoundingBox().inflate(4+modifier.getLevel()*2);
            level.getEntitiesOfClass(Projectile.class,aabb,projectile -> projectile.getOwner()!=holder&&!(projectile instanceof ManaBurstEntity)).forEach(projectile -> {
                Vec3 relative = holder.position().add(0,holder.getBbHeight()/2,0).subtract(projectile.position()).reverse();
                double force = modifier.getLevel()/(Math.pow(relative.length(),2));
                projectile.setDeltaMovement(projectile.getDeltaMovement().add(relative.normalize().scale(force)));
            });
        }
    }
}
