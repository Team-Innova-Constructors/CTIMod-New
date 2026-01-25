package com.hoshino.cti.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

@Mixin(ToolStats.class)
public class ToolStatsMixin {

    @ModifyConstant(
            method = "<clinit>",
            constant = {
                    @Constant(floatValue = 2048.0F)
            }
    )
    @Unique
    private static float AmodifyMaxValue(float constant) {
        return 16384F;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = {
                    @Constant(floatValue = 1024.0F)
            }
    )
    @Unique
    private static float BmodifyMaxValue(float constant) {
        return 8196F;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = {
                    @Constant(floatValue = 30.0F)
            }
    )
    @Unique
    private static float CmodifyMaxValue(float constant) {
        return 120F;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = {
                    @Constant(floatValue = 20.0F)
            }
    )
    @Unique
    private static float DmodifyMaxValue(float constant) {
        return 80F;
    }
}
