package com.hoshino.cti.mixin.MekMixin;

import com.hoshino.cti.register.CtiBlockEntityType;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import mekanism.generators.common.content.fission.FissionReactorValidator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FissionReactorValidator.class,remap = false)
public abstract class FissionReactorValidatorMixin extends CuboidStructureValidator<FissionReactorMultiblockData> {
    @Inject(method = "getCasingType",at = @At("HEAD"),cancellable = true)
    private void addCustomPort(BlockState state, CallbackInfoReturnable<FormationProtocol.CasingType> cir){
        Block block = state.getBlock();

        if (BlockType.is(block, CtiBlockEntityType.MekBEType.FISSION_NANO_FIX_PORT_BE_TYPE))
            cir.setReturnValue(FormationProtocol.CasingType.VALVE);
    }
}
