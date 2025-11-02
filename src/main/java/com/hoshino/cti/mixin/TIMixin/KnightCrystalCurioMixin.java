package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.content.library.helper.ScheduleHelper;
import com.xiaoyue.tinkers_ingenuity.modifiers.curio.CurioKnightVow;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = CurioKnightVow.class, remap = false)
public class KnightCrystalCurioMixin {
    /**
     * @author firyfly
     * @reason 这B太超模了，欠削
     * @数值调整 最大生命4.0->最大生命 0.2
     */
    @Overwrite
    public void onCurioTakeHurt(IToolStackView curio, LivingHurtEvent event, LivingEntity entity, DamageSource source, int level) {
        if (event.getAmount() > 0.0F) {
            ScheduleHelper.scheduleInTick("knight_vow", 40, () -> {
                float amount = event.getAmount() * 0.3F;
                entity.setAbsorptionAmount(Math.min(entity.getMaxHealth() * 0.2F, amount+entity.getAbsorptionAmount()));
            });
        }
    }
}
