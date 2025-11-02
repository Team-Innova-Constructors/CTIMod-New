package com.hoshino.cti.register;

import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.states.IStateFluidLoggable;
import mekanism.common.content.blocktype.Factory;
import mekanism.common.content.blocktype.Machine.FactoryMachine;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.factory.TileEntityFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockFactoryMachine<TILE extends TileEntityMekanism, MACHINE extends FactoryMachine<TILE>> extends BlockTile<TILE, MACHINE> {

    public BlockFactoryMachine(MACHINE machineType) {
        super(machineType);
    }

    public static class BlockFactoryMachineModel<TILE extends TileEntityMekanism> extends mekanism.common.block.prefab.BlockFactoryMachine<TILE, FactoryMachine<TILE>> implements IStateFluidLoggable {

        public BlockFactoryMachineModel(FactoryMachine<TILE> machineType) {
            super(machineType);
        }

        @Override
        public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
            return null;
        }

        @Override
        public Optional<SoundEvent> getPickupSound() {
            return Optional.empty();
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
            return null;
        }

        @Override
        public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
            return false;
        }

        @Override
        public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
            return false;
        }
    }

    public static class BlockFactory<TILE extends TileEntityFactory<?>> extends mekanism.common.block.prefab.BlockFactoryMachine<TILE, Factory<TILE>> {

        public BlockFactory(Factory<TILE> factoryType) {
            super(factoryType);
        }
    }
}
