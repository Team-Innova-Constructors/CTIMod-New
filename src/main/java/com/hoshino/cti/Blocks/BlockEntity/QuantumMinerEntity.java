package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.recipe.QuantumMinerRecipe;
import com.hoshino.cti.recipe.RecipeMap;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.ctiEnergyStore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class QuantumMinerEntity extends BlockEntity {
    public QuantumMinerEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CtiBlockEntityType.QUANTUM_MINER_ENTITY.get(), p_155229_, p_155230_);
        this.DATA = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> QuantumMinerEntity.this.PROGRESS;
                    case 1 -> QuantumMinerEntity.this.MAX_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> QuantumMinerEntity.this.PROGRESS = value;
                    case 1 -> QuantumMinerEntity.this.MAX_PROGRESS = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    protected int MAX_ENERGY = 2147483647;
    protected int MAX_TRANSFER = 2147483647;
    protected int BASE_ENERGY_PERTICK = 134217728;
    public ContainerData DATA;
    public int PROGRESS = 0;
    public int MAX_PROGRESS = 20;
    private static final IItemHandler EMPTY = new IItemHandler() {
        @Override
        public int getSlots() {
            return 0;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int i) {
            return null;
        }

        @Override
        public @NotNull ItemStack insertItem(int i, @NotNull ItemStack itemStack, boolean b) {
            return null;
        }

        @Override
        public @NotNull ItemStack extractItem(int i, int i1, boolean b) {
            return null;
        }

        @Override
        public int getSlotLimit(int i) {
            return 0;
        }

        @Override
        public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
            return false;
        }
    };

    public final ctiEnergyStore ENERGY_STORAGE = new ctiEnergyStore(getMaxEnergy(), getMaxTransfer()) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem().equals(CtiItem.compressed_singularity.get());
        }
    };

    public int getMaxEnergy() {
        return this.MAX_ENERGY;
    }

    public int getMaxTransfer() {
        return this.MAX_TRANSFER;
    }

    public int getEnergyPerTick() {
        return this.BASE_ENERGY_PERTICK;
    }

    private LazyOptional<ItemStackHandler> LazyitemStackHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> LazyenergyHandler = LazyOptional.empty();

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyitemStackHandler.cast();
        }
        if (capability == ForgeCapabilities.ENERGY) {
            return LazyenergyHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyitemStackHandler = LazyOptional.of(() -> itemStackHandler);
        LazyenergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyitemStackHandler.invalidate();
        LazyenergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.put("energy_store", ENERGY_STORAGE.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        ENERGY_STORAGE.deserializeNBT(nbt.get("energy_store"));
        super.load(nbt);
    }

    public void dropItem() {
        if (itemStackHandler.getSlots() > 0) {
            SimpleContainer container = new SimpleContainer(itemStackHandler.getSlots());
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                container.setItem(i, itemStackHandler.getStackInSlot(i));
            }
            if (this.level != null) {
                Containers.dropContents(this.level, this.worldPosition, container);
            }
        }
    }

    @Nullable
    public ctiEnergyStore getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, QuantumMinerEntity entity) {
        if (level.isClientSide) {
            return;
        }
        if (entity.itemStackHandler.getStackInSlot(0).isEmpty() || entity.itemStackHandler.getStackInSlot(0).getCount() == 0) {
            return;
        }
        if (entity.itemStackHandler.getStackInSlot(0).getItem() != CtiItem.compressed_singularity.get()) {
            return;
        }
        ItemStack output = getOutPut(level);
        if (output.isEmpty()) {
            return;
        }
        if (entity.ENERGY_STORAGE.getEnergyStored() < entity.BASE_ENERGY_PERTICK) {
            return;
        }
        if (entity.PROGRESS <= 0 && !entity.itemStackHandler.extractItem(0, 1, true).isEmpty() && entity.itemStackHandler.extractItem(0, 1, true).is(CtiItem.compressed_singularity.get())) {
            entity.itemStackHandler.extractItem(0, 1, false);
            entity.PROGRESS += 20;
        }
        BlockEntity blockEntity = level.getBlockEntity(blockPos.above());
        if (blockEntity != null) {
            LazyOptional<IItemHandler> optional = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
            IItemHandler Handler = optional.orElse(EMPTY);
            if (Handler != EMPTY && entity.PROGRESS > 0) {
                int slotAmount = Handler.getSlots();
                boolean checkoutput = false;
                int effectiveSlot = 0;
                for (int a = 0; a < slotAmount; a++) {
                    if ((Handler.getStackInSlot(a).isEmpty() || (Handler.getStackInSlot(a).is(output.getItem()) && Handler.getStackInSlot(a).getCount() + output.getCount() <= Handler.getSlotLimit(a))) && Handler.isItemValid(a, output)) {
                        checkoutput = true;
                        effectiveSlot = a;
                    }
                }
                if (!checkoutput) {
                    return;
                }
                Handler.insertItem(effectiveSlot, output, false);
                entity.ENERGY_STORAGE.extractEnergy(entity.BASE_ENERGY_PERTICK, false);
                entity.PROGRESS--;
                entity.setChanged();
            }
        }
    }

    public static ItemStack getOutPut(Level level) {
        List<QuantumMinerRecipe> list = List.copyOf(RecipeMap.MinerRecipeList);
        if (list.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int length = list.size();
        QuantumMinerRecipe recipe = list.get(EtSHrnd().nextInt(length));
        ItemStack stack = recipe.getResultItem();
        int count = (int) recipe.getChance();
        if (count == 0 && EtSHrnd().nextFloat() > recipe.getChance()) {
            stack = ItemStack.EMPTY;
        } else {
            float chance = recipe.getChance() - count;
            count += EtSHrnd().nextFloat() > chance ? -1 : 0;
            stack.setCount(stack.getCount() + count);
        }
        return stack;
    }
}
