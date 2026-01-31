package com.hoshino.cti.Blocks.BlockEntity.tinker.refinery;

import com.hoshino.cti.api.interfaces.IMachineAirHandlerProvider;
import com.hoshino.cti.api.interfaces.IOreRateCondition;
import com.hoshino.cti.mixin.TconMixin.MeltingModuleInventoryAccessor;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.util.ConditionalOreRate;
import com.hoshino.cti.util.ICtiMeltingModule;
import com.hoshino.cti.util.ICtiMeltingRecipe;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;

import javax.annotation.Nullable;
import java.util.List;

public class RefineryControllerBlockEntity extends HeatingStructureBlockEntity implements IMachineAirHandlerProvider {
    protected RefineryControllerBlockEntity(BlockEntityType<? extends HeatingStructureBlockEntity> type, BlockPos pos, BlockState state, Component name) {
        super(type, pos, state, name);
    }
    public RefineryControllerBlockEntity(BlockPos pos, BlockState state) {
        this(CtiBlockEntityType.REFINERY.get(), pos, state, NAME);
    }



    private static final int CAPACITY_PER_BLOCK = FluidValues.INGOT * 64;
    private static final Component NAME = Component.translatable("gui.cti.refinery");
    private static final int BLOCKS_PER_FUEL = 54;
    protected final MachineAirHandler machineAirHandler = new MachineAirHandler(PressureTier.TIER_TWO, 128000) {
        @Override
        public float getCriticalPressure() {
            return 40;
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public Direction getSideLeaking() {
            return null;
        }

        @Override
        public List<Connection> getConnectedAirHandlers(BlockEntity ownerTE) {
            return super.getConnectedAirHandlers(ownerTE);
        }
    };
    public ConditionalOreRate refineryRate;

    @Override
    protected HeatingStructureMultiblock<?> createMultiblock() {
        return new RefineryMultiblock(this);
    }

    @Override
    protected MeltingModuleInventory createMeltingInventory() {
        if (refineryRate==null)
            refineryRate = new ConditionalOreRate(new IOreRateCondition.BiRateCondition(()->machineAirHandler.getPressure()<-0.5,3,9));
        return new RefineryMeltingModuleInventory(this,tank,refineryRate);
    }

    @Override
    public MachineAirHandler getAirHandler() {
        return machineAirHandler;
    }

    public static class RefineryMeltingModuleInventory extends MeltingModuleInventory{
        protected final RefineryControllerBlockEntity refinery;
        protected final IMeltingContainer.IOreRate iOreRate;
        public RefineryMeltingModuleInventory(MantleBlockEntity parent, IFluidHandler fluidHandler, IMeltingContainer.IOreRate oreRate) {
            super(parent, fluidHandler, oreRate);
            refinery = parent instanceof RefineryControllerBlockEntity be?be:null;
            iOreRate = oreRate;
        }
        @Override
        protected boolean tryFillTank(int index, IMeltingRecipe recipe) {
            if (super.tryFillTank(index, recipe)) {
                if (recipe instanceof ICtiMeltingRecipe meltingRecipe)
                    meltingRecipe.cti$handleByproducts(getModule(index),fluidHandler, iOreRate);
                else recipe.handleByproducts(getModule(index), fluidHandler);
                return true;
            }
            return false;
        }

        @Override
        public void heatItems(int temperature) {
            var modules = ((MeltingModuleInventoryAccessor)this).cti$getModules();
            var boost = 4;
            if (refinery.machineAirHandler.getPressure()<-0.5) boost*=8;
            for (MeltingModule module : modules) {
                if (module != null) {
                    if (!module.getStack().isEmpty()&&refinery.machineAirHandler.getPressure()<1) refinery.machineAirHandler.addAir(5);
                    ((ICtiMeltingModule)module).cti$heatItem(temperature,boost);
                }
            }
        }
    }

    @Override
    protected void serverTick(Level level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        this.machineAirHandler.tick(this);
    }

    @Override
    protected void heat() {
        if (structure == null || level == null) {
            return;
        }
        if (structure.hasTanks()) {
            boolean entityMelted = false;
            if (tick == 12) {
                entityMelted = entityModule.interactWithEntities();
            }
            switch (tick % 4) {
                case 0:
                    if (!fuelModule.hasFuel()) {
                        if (entityMelted) {
                            fuelModule.findFuel(true);
                        } else {
                            if (meltingInventory.canHeat(fuelModule.findFuel(false))) {
                                fuelModule.findFuel(true);
                            }
                        }
                    }
                    break;
                case 1:
                    if (fuelModule.hasFuel()) {
                        (meltingInventory).heatItems(fuelModule.getTemperature());
                    } else {
                        meltingInventory.coolItems();
                    }
                    break;
                case 3: {
                    boolean hasFuel = fuelModule.hasFuel();
                    BlockState state = getBlockState();
                    if (state.getValue(ControllerBlock.ACTIVE) != hasFuel) {
                        level.setBlockAndUpdate(worldPosition, state.setValue(ControllerBlock.ACTIVE, hasFuel));
                    }
                    fuelModule.decreaseFuel(fuelRate);
                    break;
                }
            }
        }
    }

    @Override
    protected void setStructure(@Nullable HeatingStructureMultiblock.StructureData structure) {
        super.setStructure(structure);
        if (structure != null) {
            int dx = structure.getInnerX(), dy = structure.getInnerY(), dz = structure.getInnerZ();
            tank.setCapacity(CAPACITY_PER_BLOCK * (dx + 2) * (dy + 1) * (dz + 2));
            meltingInventory.resize(dx * dy * dz, dropItem);
            fuelRate = 1 + (2 * ((dx+2) * dy) + 2 * (dy * dz) + ((dx+2) * (dz+2))) / BLOCKS_PER_FUEL;
        }
    }

    @Override
    protected boolean isDebugItem(ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(Capability<T> capability, @org.jetbrains.annotations.Nullable Direction facing) {
        if (capability== PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY) return LazyOptional.of(()->this.machineAirHandler).cast();
        return super.getCapability(capability, facing);
    }
}
