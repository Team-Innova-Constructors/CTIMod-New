package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.register.CtiBlockEntityType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.multiblock.ServantTileEntity;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler;
import slimeknights.tconstruct.smeltery.block.entity.tank.SmelteryTank;

import java.util.ArrayList;
import java.util.List;

public class AlloyCentrifugeEntity extends BlockEntity {
    public AlloyCentrifugeEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CtiBlockEntityType.ALLOY_CENTRIFUGE.get(), p_155229_, p_155230_);
    }

    protected static List<AlloyRecipe> list = new ArrayList<>();

    private LazyOptional<IAirHandlerMachine> airHandler = LazyOptional.empty();

    private MachineAirHandler machineAirHandler = new MachineAirHandler(PressureTier.TIER_TWO, 12800) {
        @Override
        public float getCriticalPressure() {
            return 40;
        }

        @Nullable
        @Override
        public Direction getSideLeaking() {
            return null;
        }

        @Override
        public List<Connection> getConnectedAirHandlers(BlockEntity ownerTE) {
            return super.getConnectedAirHandlers(ownerTE);
        }
    };

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY) {
            return airHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        this.airHandler = LazyOptional.of(() -> this.machineAirHandler);
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        this.airHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("AirHandler", this.machineAirHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.machineAirHandler.deserializeNBT(nbt.getCompound("AirHandler"));
    }


    public static void tick(Level level, BlockPos blockPos, BlockState state, AlloyCentrifugeEntity entity) {
        if (level.isClientSide) {
            return;
        }
        entity.machineAirHandler.tick(entity);
        if (entity.machineAirHandler.getVolume() < 100 * (int) entity.machineAirHandler.getPressure() || entity.machineAirHandler.getPressure() < 3) {
            return;
        }
        if (level.getGameTime() % Math.max(2, 12 - (int) entity.machineAirHandler.getPressure()) != 0) {
            return;
        }
        if (list.isEmpty()) {
            list = level.getRecipeManager().getAllRecipesFor(TinkerRecipeTypes.ALLOYING.get());
        }
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockEntity blockEntity = level.getBlockEntity(entity.getBlockPos().relative(direction.getOpposite()));
        if (blockEntity == null) {
            return;
        }
        if (blockEntity instanceof ServantTileEntity servantTileEntity) {
            BlockEntity master = servantTileEntity.getMasterPos() != null ? level.getBlockEntity(servantTileEntity.getMasterPos()) : null;
            if (master instanceof ISmelteryTankHandler smelteryTankHandler) {
                List<FluidStack> OutputfluidStacks = new ArrayList<>();
                SmelteryTank smelteryTank = smelteryTankHandler.getTank();
                for (int i = 0; i < smelteryTank.getTanks(); i++) {
                    FluidStack stack = smelteryTank.getFluidInTank(i);
                    AlloyRecipe recipe = getRecipe(stack);
                    FluidStack InputFluid = FluidStack.EMPTY;
                    if (recipe != null && recipe.getOutput().getAmount() <= stack.getAmount()) {
                        InputFluid = recipe.getOutput();
                        List<List<FluidStack>> listList = recipe.getDisplayInputs();
                        for (List<FluidStack> ls : listList) {
                            OutputfluidStacks.addAll(ls);
                        }
                    }
                    boolean canFill = false;
                    for (FluidStack fluidStack : OutputfluidStacks) {
                        int amount = smelteryTank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);
                        if (amount == fluidStack.getAmount()) {
                            canFill = true;
                        } else {
                            canFill = false;
                            break;
                        }
                    }
                    boolean canDrain = smelteryTank.drain(InputFluid, IFluidHandler.FluidAction.SIMULATE).getAmount() == InputFluid.getAmount() && !InputFluid.isEmpty();
                    if (canFill && canDrain) {
                        entity.machineAirHandler.addAir(-100 * (int) entity.machineAirHandler.getPressure());
                        smelteryTank.drain(InputFluid, IFluidHandler.FluidAction.EXECUTE);
                        for (FluidStack fluidStack : OutputfluidStacks) {
                            smelteryTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        }
                        return;
                    }
                }
            }
        }
    }

    public static AlloyRecipe getRecipe(FluidStack fluidStack) {
        AlloyRecipe recipe = null;
        if (list.isEmpty()) {
            return recipe;
        }
        for (AlloyRecipe recipes : list) {
            if (recipes.getOutput().getFluid().isSame(fluidStack.getFluid())) {
                recipe = recipes;
                break;
            }
        }
        return recipe;
    }
}
