package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.IntSupplier;

@Mixin(value = RegenTrait.class, remap = false)
public abstract class RegenTaintMixin extends MobTrait {
    public RegenTaintMixin(IntSupplier color) {
        super(color);
    }

    /**
     * @author firefly
     * @reason 最新更改:
     * <br><h5>I.大幅度降低了基础恢复(每秒1.5%x等级->0.05%x等级)
     * <br>II.现在首次进入半血会附带黎明效果,快速恢复一部分生命,只触发一次
     * <br>III.触发黎明后怪物造成的伤害提升10%</h5>
     */
    @Overwrite
    public void tick(LivingEntity mob, int level) {
        if (!mob.getLevel().isClientSide()) {
            if (mob.tickCount % 20 == 0) {
                var data = mob.getPersistentData();
                boolean shouldAdd = !data.contains("cti_liming");
                if (mob.getHealth() <= mob.getMaxHealth() * 0.5f && shouldAdd) {
                    mob.getLevel().playSound(null, mob.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 1, 0.5f);
                    mob.getPersistentData().putInt("cti_liming", 5);
                }
                var l = data.getInt("cti_liming");
                boolean shouldExtraHeal = data.getInt("cti_liming") > 1;
                float healAmount = 0;
                if (shouldExtraHeal) {
                    healAmount += mob.getMaxHealth() * 0.02f * level;
                    data.putInt("cti_liming", l - 1);
                }
                healAmount += 0.005f * mob.getMaxHealth() * level;
                mob.heal(healAmount);
            }
        }
    }
}
