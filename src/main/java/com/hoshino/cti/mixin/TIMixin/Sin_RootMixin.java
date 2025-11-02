package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.modifiers.misc.SinRoot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

@Mixin(value = SinRoot.class, remap = false)
public class Sin_RootMixin {
    /**
     * @author Firefly
     * @reason <h4></h4>欠削无需多言
     * @数值调整 <h4></h4>最大生命4%->最大生命3%
     */
    @Overwrite
    private float getBonus(LivingEntity entity) {
        return entity.getMaxHealth() * 0.03F;
    }

    /**
     * @author Firefly
     * @reason 无需多言
     */
    @Overwrite
    public float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        return actualDamage + this.getBonus(target);
    }

    /**
     * @author Firefly
     * @reason 无需多言
     */
    @Overwrite
    public void onArrowHitTarget(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        arrow.setBaseDamage(arrow.getBaseDamage() + (double) this.getBonus(target));
    }
}
