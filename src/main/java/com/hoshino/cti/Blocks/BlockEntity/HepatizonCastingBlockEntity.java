package com.hoshino.cti.Blocks.BlockEntity;


import com.hoshino.cti.api.interfaces.IConditionalSpeedCastingBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;

public class HepatizonCastingBlockEntity extends CastingBlockEntity implements IConditionalSpeedCastingBlockEntity {
    protected HepatizonCastingBlockEntity(BlockEntityType<?> beType, BlockPos pos, BlockState state, RecipeType<ICastingRecipe> castingType, RecipeType<MoldingRecipe> moldingType, TagKey<Item> emptyCastTag) {
        super(beType, pos, state, castingType, moldingType, emptyCastTag);
    }

    @Override
    public int modifyTotalCoolingTime(FluidStack fluidStack, ICastingRecipe iCastingRecipe, int i) {
        return (int) (i/ 4);
    }

    @Override
    public int getBoost(int i) {
        return 0;
    }

    public static class Table extends HepatizonCastingBlockEntity {
        public Table(BlockPos pos, BlockState state) {
            super(CtiBlockEntityType.HEPATIZON_TABLE.get(), pos, state, TinkerRecipeTypes.CASTING_TABLE.get(), TinkerRecipeTypes.MOLDING_TABLE.get(), TinkerTags.Items.TABLE_EMPTY_CASTS);
        }
    }
    public static class Basin extends HepatizonCastingBlockEntity {
        public Basin(BlockPos pos, BlockState state) {
            super(CtiBlockEntityType.HEPATIZON_BASIN.get(), pos, state, TinkerRecipeTypes.CASTING_BASIN.get(), TinkerRecipeTypes.MOLDING_BASIN.get(), TinkerTags.Items.BASIN_EMPTY_CASTS);
        }
    }

}
