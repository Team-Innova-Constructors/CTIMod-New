package com.hoshino.cti.Entity.ai.aetherSlider;

import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import com.hoshino.cti.util.mixin.slider.ISliderMixin;
import net.minecraft.world.entity.ai.goal.Goal;

public class LaserAttackGoal extends Goal {
    private final Slider slider;
    public final ISliderMixin sliderExtra;

    public LaserAttackGoal(Slider slider, ISliderMixin sliderExtra) {
        this.slider = slider;
        this.sliderExtra = sliderExtra;
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
