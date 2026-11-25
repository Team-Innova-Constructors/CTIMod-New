package com.hoshino.cti.Blocks.BlockEntity.mek;

import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.register.CtiItem;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorCasing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FissionNanoFixPortBlockEntity extends TileEntityFissionReactorCasing {
    public FissionNanoFixPortBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.MekBlock.FISSION_NANO_FIX_PORT_BLOCK,pos, state);
    }
    public int fixCount = 0;
    public static int MAX_FIX_ITEM;
    public static int FIX_COUNT_PER_ITEM;
    public static int MAX_FIX_COUNT;
    static {
        MAX_FIX_ITEM = 10;
        FIX_COUNT_PER_ITEM = 100;
        MAX_FIX_COUNT = 1000;
    }
    public IItemHandler itemHandler = null;

    @Override
    protected boolean onUpdateServer(FissionReactorMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        if (multiblock.isFormed()&&level!=null&&level.getGameTime()%2==0) {
            if (multiblock.reactorDamage > -MAX_FIX_COUNT && fixCount > 0) {
                fixCount -= 1;
                multiblock.reactorDamage -= 1;
            }
        }
        return needsPacket;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("fixCount",fixCount);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fixCount = pTag.getInt("fixCount");
    }

    public IItemHandler getItemHandler(){
        if (itemHandler==null)
            itemHandler = new IItemHandler() {
                @Override
                public int getSlots() {
                    return 1;
                }

                @Override
                public @NotNull ItemStack getStackInSlot(int slot) {
                    return new ItemStack(CtiItem.NANOMACHINE.get(),fixCount/FIX_COUNT_PER_ITEM);
                }

                @Override
                public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                    var remain = stack.copy();
                    if (fixCount<MAX_FIX_COUNT&&isItemValid(slot,stack)){
                        var needed = (MAX_FIX_COUNT-fixCount)/FIX_COUNT_PER_ITEM;
                        if (needed>0){
                            needed = Math.min(needed,remain.getCount());
                            remain.shrink(needed);
                            if (!simulate)
                                fixCount+=needed*FIX_COUNT_PER_ITEM;
                        }
                    }
                    return remain;
                }

                @Override
                public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                    var stack = ItemStack.EMPTY;
                    if (fixCount>FIX_COUNT_PER_ITEM&&slot==0){
                        amount = Math.min(amount,fixCount/FIX_COUNT_PER_ITEM);
                        stack = new ItemStack(CtiItem.NANOMACHINE.get(),amount);
                        if (!simulate){
                            fixCount-=amount*FIX_COUNT_PER_ITEM;
                        }
                    }
                    return stack;
                }

                @Override
                public int getSlotLimit(int slot) {
                    return MAX_FIX_ITEM;
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return stack.is(CtiItem.NANOMACHINE.get())&&slot==0;
                }
            };
        return itemHandler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability== ForgeCapabilities.ITEM_HANDLER&&getMultiblock()!=null) {
            return getStructure()!=null?LazyOptional.of(this::getItemHandler).cast():super.getCapability(capability);
        }
        return super.getCapability(capability, side);
    }
}
