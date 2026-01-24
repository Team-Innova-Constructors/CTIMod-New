package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.api.interfaces.IConfigurableFaucetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity;

@Mixin(value = FaucetBlockEntity.class,remap = false)
public class FaucetBlockEntityMixin extends MantleBlockEntity {
    public FaucetBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @ModifyArg(method = "doTransfer",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/IFluidHandler;drain(ILnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;",ordinal = 0),index = 0)
    public int modifyMaxDrain(int maxDrain){
        if (getBlockState().getBlock() instanceof IConfigurableFaucetBlock faucet) return faucet.getMaxTransfer();
        return maxDrain;
    }
    @ModifyArg(method = "pour",at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"),index = 1)
    public int modifyMaxInsert(int a){
        if (getBlockState().getBlock() instanceof IConfigurableFaucetBlock faucet) return faucet.getMaxTransfer();
        return a;
    }

}
