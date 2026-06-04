package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Slider.class,remap = false)
public abstract class SliderMixin extends PathfinderMob {
    protected SliderMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isCritical() {
        return this.getHealth() <= (this.getMaxHealth() * 0.25F);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getVelocityIncrease() {
        float healthRatio = this.getHealth() / this.getMaxHealth();
        if (healthRatio > 1.0F) healthRatio = 1.0F;
        if (healthRatio < 0.0F) healthRatio = 0.0F;
        if (this.isCritical()) {
            return 0.045F - (healthRatio * 0.04F);
        } else {
            return 0.035F - (healthRatio * 0.0133F);
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getMaxVelocity() {
        float healthRatio = this.getHealth() / this.getMaxHealth();
        return 3.5F - healthRatio;
    }
}
