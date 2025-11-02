package com.hoshino.cti.mixin.MowzieMixin;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.hoshino.cti.util.ISunStrikeMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntitySunstrike.class,remap = false)
public class EntitySunstrikeMixin implements ISunStrikeMixin {
    @Shadow private int strikeTime;
    @Shadow private int prevStrikeTime;

    @Override
    public int cti$getStrikeTime() {
        return strikeTime;
    }

    @Override
    public int cti$getPrevStrikeTime() {
        return prevStrikeTime;
    }

    @Override
    public void cti$setStrikeTime(int i) {
        this.strikeTime = i;
    }

    @Override
    public void cti$setPrevStrikeTime(int i) {
        this.prevStrikeTime = i;
    }
}
