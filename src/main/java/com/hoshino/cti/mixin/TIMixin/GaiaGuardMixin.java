package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.compat.botania.GaiaGuard;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;

@Mixin(value = GaiaGuard.class, remap = false)
public class GaiaGuardMixin {
    /**
     * @author Firefly
     * @reason 无需多言, 222格挡诗人啊？
     * @数值调整 格挡值222->100
     */
    @Overwrite
    public float onModifyTakeDamage(IToolStackView armor, int level, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player) {
            float maxReduce = Math.min(100.0F, amount);
            if (ManaItemHandler.instance().requestManaExactForTool(player.getItemBySlot(slot), player, (int) (maxReduce * 50.0F), true)) {
                return amount - maxReduce;
            }
        }

        return amount;
    }
}
