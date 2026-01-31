package com.hoshino.cti.Blocks.BlockEntity.tinker;

import com.hoshino.cti.api.interfaces.IConditionalSpeedCastingBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.client.util.TintColor;
import me.desht.pneumaticcraft.common.block.entity.IHeatExchangingTE;
import me.desht.pneumaticcraft.common.block.entity.IHeatTinted;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import me.desht.pneumaticcraft.common.heat.SyncedTemperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;

import java.util.List;

public class ZirconiumCastingBlockEntity extends CastingBlockEntity implements IConditionalSpeedCastingBlockEntity, IHeatTinted, IHeatExchangingTE {
    protected final IHeatExchangerLogic heatExchanger;
    private final LazyOptional<IHeatExchangerLogic> heatCap;
    protected final SyncedTemperature syncedTemperature;
    protected ZirconiumCastingBlockEntity(BlockEntityType<?> beType, BlockPos pos, BlockState state, RecipeType<ICastingRecipe> castingType, RecipeType<MoldingRecipe> moldingType, TagKey<Item> emptyCastTag) {
        super(beType, pos, state, castingType, moldingType, emptyCastTag);
        this.heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        this.heatCap = LazyOptional.of(() -> this.heatExchanger);
        this.syncedTemperature = new SyncedTemperature(this.heatExchanger);
        this.heatExchanger.setThermalCapacity(20f);
    }

    @Override
    public int modifyTotalCoolingTime(FluidStack fluidStack, ICastingRecipe iCastingRecipe, int i) {
        return i;
    }

    @Override
    public int getBoost(int i) {
        var boost =  Math.max(0,((250-Math.max(0,heatExchanger.getTemperature()))/5)-1)+(2*Math.max(0,0-heatExchanger.getTemperature()));
        this.heatExchanger.addHeat(boost/2);
        return (int) boost;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap== PNCCapabilities.HEAT_EXCHANGER_CAPABILITY) return heatCap.cast();
        return super.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability== PNCCapabilities.HEAT_EXCHANGER_CAPABILITY) return heatCap.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public List<Direction> ejectSides() {
        return List.of(Direction.DOWN);
    }

    @Override
    public void onServerTickEnd() {
        this.syncedTemperature.tick();
        this.heatExchanger.tick();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.heatCap.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag tags) {
        super.saveAdditional(tags);
        tags.put("heatExchanger",this.heatExchanger.serializeNBT());
    }

    @Override
    public void load(CompoundTag tags) {
        super.load(tags);
        this.heatExchanger.deserializeNBT(tags.getCompound("heatExchanger"));
    }

    @Override
    public @Nullable IHeatExchangerLogic getHeatExchanger(Direction direction) {
        return this.heatExchanger;
    }

    @Override
    public TintColor getColorForTintIndex(int i) {
        return HeatUtil.getColourForTemperature(this.syncedTemperature.getSyncedTemp());
    }

    public static class Table extends ZirconiumCastingBlockEntity {
        public Table(BlockPos pos, BlockState state) {
            super(CtiBlockEntityType.ZR_ALLOY_TABLE.get(), pos, state, TinkerRecipeTypes.CASTING_TABLE.get(), TinkerRecipeTypes.MOLDING_TABLE.get(), TinkerTags.Items.TABLE_EMPTY_CASTS);
        }
    }
    public static class Basin extends ZirconiumCastingBlockEntity {
        public Basin(BlockPos pos, BlockState state) {
            super(CtiBlockEntityType.ZR_ALLOY_BASIN.get(), pos, state, TinkerRecipeTypes.CASTING_BASIN.get(), TinkerRecipeTypes.MOLDING_BASIN.get(), TinkerTags.Items.BASIN_EMPTY_CASTS);
        }
    }
}
