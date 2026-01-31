package com.hoshino.cti.Blocks.BlockEntity.tinker;

import com.hoshino.cti.register.CtiBlockEntityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.fluid.FluidTankAnimated;
import slimeknights.tconstruct.library.recipe.alloying.IMutableAlloyTank;
import slimeknights.tconstruct.library.utils.NBTTags;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.controller.MelterBlock;
import slimeknights.tconstruct.smeltery.block.entity.ITankBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.FuelModule;
import slimeknights.tconstruct.smeltery.block.entity.module.alloying.SingleAlloyingModule;
import slimeknights.tconstruct.smeltery.block.entity.module.alloying.SmelteryAlloyTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Predicate;

public class AdvancedAlloyerBlockEntity extends MantleBlockEntity implements ITankBlockEntity {
    public static final BlockEntityTicker<AdvancedAlloyerBlockEntity> SERVER_TICKER = (level, pos, state, self) -> self.tick(level, pos, state);

    protected static int CFG_TANK_CAPACITY = 16000;
    protected static int CFG_ALLOYING_CYCLE = 4;

    @Getter
    protected final FluidTankAnimated tank;
    private final LazyOptional<IFluidHandler> tankHolder;


    @Getter
    private final AdvancedMixerAlloyTank alloyTank;
    private final SingleAlloyingModule alloyingModule;
    @Getter
    private final FuelModule fuelModule;

    @Getter @Setter
    private int lastStrength = -1;

    private int tick;

    public AdvancedAlloyerBlockEntity(BlockPos pos, BlockState state) {
        this(CtiBlockEntityType.ADVANCED_ALLOYER.get(), pos, state);
    }

    protected AdvancedAlloyerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        CFG_TANK_CAPACITY = 32000;
        CFG_ALLOYING_CYCLE = 2;
        this.fuelModule = new FuelModule(this, () -> Collections.singletonList(this.worldPosition.below()));
        this.tank = new FluidTankAnimated(CFG_TANK_CAPACITY, this);
        this.tankHolder = LazyOptional.of(() -> tank);
        this.alloyTank = new AdvancedMixerAlloyTank(this, tank);
        this.alloyingModule = new SingleAlloyingModule(this, alloyTank);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return tankHolder.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.tankHolder.invalidate();
    }

    private boolean isFormed() {
        BlockState state = this.getBlockState();
        return state.hasProperty(MelterBlock.IN_STRUCTURE) && state.getValue(MelterBlock.IN_STRUCTURE);
    }

    private void tick(Level level, BlockPos pos, BlockState state) {
        if (isFormed()) {
            switch (tick) {
                case 0 -> {
                    alloyTank.setTemperature(fuelModule.findFuel(false));
                    if (!fuelModule.hasFuel() && alloyingModule.canAlloy()) {
                        fuelModule.findFuel(true);
                    }
                }
                case 1 -> {
                    boolean hasFuel = fuelModule.hasFuel();
                    if (state.getValue(ControllerBlock.ACTIVE) != hasFuel) {
                        level.setBlockAndUpdate(pos, state.setValue(ControllerBlock.ACTIVE, hasFuel));
                        BlockPos down = pos.below();
                        BlockState downState = level.getBlockState(down);
                        if (downState.is(TinkerTags.Blocks.FUEL_TANKS) && downState.hasProperty(ControllerBlock.ACTIVE) && downState.getValue(ControllerBlock.ACTIVE) != hasFuel) {
                            level.setBlockAndUpdate(down, downState.setValue(ControllerBlock.ACTIVE, hasFuel));
                        }
                    }
                    if (hasFuel) {
                        alloyTank.setTemperature(fuelModule.getTemperature());
                        alloyingModule.doAlloy();
                        fuelModule.decreaseFuel(1);
                    }
                }
            }
        } else if (tick == 1 && fuelModule.hasFuel()) {
            fuelModule.decreaseFuel(1);
        }
        tick = (tick + 1) % CFG_ALLOYING_CYCLE;
    }

    public void neighborChanged(Direction side) {
        alloyTank.checkUpdate(side);
    }

    @Override
    protected boolean shouldSyncOnUpdate() {
        return true;
    }

    @Override
    public void saveSynced(CompoundTag tag) {
        super.saveSynced(tag);
        tag.put(NBTTags.TANK, tank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        fuelModule.writeToTag(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        tank.readFromNBT(nbt.getCompound(NBTTags.TANK));
        fuelModule.readFromTag(nbt);
    }

    @Nullable
    public static <CAST extends AdvancedAlloyerBlockEntity, RET extends BlockEntity> BlockEntityTicker<RET> getTicker(Level level, BlockEntityType<RET> check, BlockEntityType<CAST> casting) {
        if (level.isClientSide) return null;
        return BlockEntityHelper.castTicker(check, casting, SERVER_TICKER);
    }

    @RequiredArgsConstructor
    public static class AdvancedMixerAlloyTank implements IMutableAlloyTank {
        @Getter
        @Setter
        private int temperature = 0;
        private final AdvancedAlloyerBlockEntity blockEntity;
        private final IFluidHandler outputTank;

        private boolean shouldUpdate = true;

        @org.jetbrains.annotations.Nullable
        private SmelteryAlloyTank smelteryAlloyTank;
        @org.jetbrains.annotations.Nullable
        private Predicate<FluidStack> fluidFilter;

        @Override
        public FluidStack drain(int tank, FluidStack fluidStack) {
            this.findSmeltery();
            return smelteryAlloyTank!=null?smelteryAlloyTank.drain(tank,fluidStack):FluidStack.EMPTY;
        }

        @Override
        public int getTanks() {
            this.findSmeltery();
            return this.smelteryAlloyTank!=null?this.smelteryAlloyTank.getTanks():0;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            this.findSmeltery();
            return this.smelteryAlloyTank!=null?this.smelteryAlloyTank.getFluidInTank(tank):FluidStack.EMPTY;
        }

        @Override
        public boolean canFit(FluidStack fluid, int removed) {
            findSmeltery();
            if (this.fluidFilter!=null&&!this.fluidFilter.test(fluid)) return false;
            return outputTank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount();
        }

        @Override
        public int fill(FluidStack fluidStack) {
            return outputTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        }

        protected void findSmeltery(){
            if (shouldUpdate) {
                var pos = blockEntity.getBlockPos();
                var level = blockEntity.getLevel();
                if (level == null) return;
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.DOWN) continue;
                    var fetchPos = pos.relative(direction);
                    var be = level.getBlockEntity(fetchPos);
                    if (be instanceof SmelteryInputOutputBlockEntity.SmelteryFluidIO fluidIO) {
                        var masterPos = fluidIO.getMasterPos();
                        if (masterPos != null && level.getBlockEntity(masterPos) instanceof HeatingStructureBlockEntity heating) {
                            this.smelteryAlloyTank = new SmelteryAlloyTank(heating.getTank());
                            if (fluidIO instanceof DuctBlockEntity duct)
                                this.fluidFilter = fluid -> duct.getItemHandler().getFluid().isFluidEqual(fluid);
                            break;
                        }
                    }
                }
                shouldUpdate = false;
            }
        }

        public void checkUpdate(Direction direction){
            if (direction == Direction.DOWN) {
                return;
            }
            this.shouldUpdate = true;
            this.smelteryAlloyTank = null;
        }
    }
}