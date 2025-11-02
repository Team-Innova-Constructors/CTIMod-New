package com.hoshino.cti.mixin.MekMixin;

import com.hoshino.cti.Temp;
import mekanism.api.Upgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(remap = false, value = Upgrade.class)
public class UpgradeMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static String getName(String s) {
        Temp.name = s;
        return s;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static int toFullStack(int i) {
        return Temp.name.equals("speed") || Temp.name.equals("energy") ? 64 : i;
    }
}
