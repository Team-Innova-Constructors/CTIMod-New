package com.hoshino.cti.util;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface IMixinMachineTile {
    List<BlockPos> cti$getEnergyInputList();
    List<BlockPos> cti$getEnergyOutputList();
    List<BlockPos> cti$getFluidInputList();
    List<BlockPos> cti$getFluidOutputList();
    List<BlockPos> cti$getItemInputList();
    List<BlockPos> cti$getItemOutputList();
    List<BlockPos> cti$getChemicalInputList();
    List<BlockPos> cti$getChemicalOutputList();

    void cti$addEnergyInput(BlockPos blockPos);
    void cti$addEnergyOutput(BlockPos blockPos);
    void cti$addFluidInput(BlockPos blockPos);
    void cti$addFluidOutput(BlockPos blockPos);
    void cti$addItemInput(BlockPos blockPos);
    void cti$addItemOutput(BlockPos blockPos);
    void cti$addChemicalInput(BlockPos blockPos);
    void cti$addChemicalOutput(BlockPos blockPos);
    void cti$clearCachedPorts();
}
