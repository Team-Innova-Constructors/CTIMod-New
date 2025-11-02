package com.hoshino.cti.mixin.bugFix;


import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = "com.creeping_creeper.tinkers_thinking.common.things.block.entity.DryingRackBlockEntity$1",remap = false)
public class DryingRackBlockEntity$1Mixin extends ItemStackHandler {
    @Unique
    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

}
