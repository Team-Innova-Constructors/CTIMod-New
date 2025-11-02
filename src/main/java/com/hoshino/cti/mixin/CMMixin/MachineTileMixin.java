package com.hoshino.cti.mixin.CMMixin;
/*
import com.hoshino.cti.util.IMixinMachineTile;
import fr.frinn.custommachinery.api.machine.MachineTile;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(value = MachineTile.class,remap = false)
public class MachineTileMixin implements IMixinMachineTile {
    @Unique
    @Override
    public List<BlockPos> cti$getEnergyInputList() {
        return cti$listEnergyInput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getEnergyOutputList() {
        return cti$listEnergyOutput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getFluidInputList() {
        return cti$listFluidInput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getFluidOutputList() {
        return cti$listFluidOutput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getItemInputList() {
        return cti$listItemInput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getItemOutputList() {
        return cti$listItemOutput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getChemicalInputList() {
        return cti$listChemicalInput;
    }
    @Unique
    @Override
    public List<BlockPos> cti$getChemicalOutputList() {
        return cti$listChemicalOutput;
    }
    @Unique
    @Override
    public void cti$addEnergyInput(BlockPos blockPos) {
//        if (cti$listEnergyInput==null){
//            cti$listEnergyInput = List.of();
//        }
        if(cti$listEnergyInput.contains(blockPos)) return;
        cti$listEnergyInput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addEnergyOutput(BlockPos blockPos) {
//        if (cti$listEnergyOutput==null){
//            cti$listEnergyOutput = List.of();
//        }
        if(cti$listEnergyOutput.contains(blockPos)) return;
        cti$listEnergyOutput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addFluidInput(BlockPos blockPos) {
//        if (cti$listFluidInput==null){
//            cti$listFluidInput = List.of();
//        }
        if(cti$listFluidInput.contains(blockPos)) return;
        cti$listFluidInput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addFluidOutput(BlockPos blockPos) {
//        if (cti$listFluidOutput==null){
//            cti$listFluidOutput = List.of();
//        }
        if(cti$listFluidOutput.contains(blockPos)) return;
        cti$listFluidOutput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addItemInput(BlockPos blockPos) {
//        if (cti$listItemInput==null){
//            cti$listItemInput = List.of();
//        }
        if(cti$listItemInput.contains(blockPos)) return;
        cti$listItemInput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addItemOutput(BlockPos blockPos) {
//        if (cti$listItemOutput==null){
//            cti$listItemOutput = List.of();
//        }
        if(cti$listItemOutput.contains(blockPos)) return;
        cti$listItemOutput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addChemicalInput(BlockPos blockPos) {
//        if (cti$listChemicalInput==null){
//            cti$listChemicalInput = List.of();
//        }
        if(cti$listChemicalInput.contains(blockPos)) return;
        cti$listChemicalInput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$addChemicalOutput(BlockPos blockPos) {
//        if (cti$listChemicalOutput==null){
//            cti$listChemicalOutput = List.of();
//        }
        if(cti$listChemicalOutput.contains(blockPos)) return;
        cti$listChemicalOutput.add(blockPos);
    }
    @Unique
    @Override
    public void cti$clearCachedPorts(){
        cti$listChemicalOutput.clear();
    }
    @Unique List<BlockPos> cti$listEnergyInput = List.of();
    @Unique List<BlockPos> cti$listEnergyOutput = List.of();
    @Unique List<BlockPos> cti$listItemInput = List.of();
    @Unique List<BlockPos> cti$listItemOutput = List.of();
    @Unique List<BlockPos> cti$listFluidInput = List.of();
    @Unique List<BlockPos> cti$listFluidOutput = List.of();
    @Unique List<BlockPos> cti$listChemicalInput = List.of();
    @Unique List<BlockPos> cti$listChemicalOutput = List.of();
}

 */
