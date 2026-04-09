package com.hoshino.cti.Blocks.BlockEntity;

import appbot.ae2.ManaGenericStackInvStorage;
import appeng.blockentity.misc.InterfaceBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaCollector;

public class ManaInterfaceBE extends InterfaceBlockEntity implements ManaCollector {
    public ManaInterfaceBE(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }
    public ManaInterfaceBE(BlockPos blockPos,BlockState blockState){
        this(CtiBlockEntityType.MANA_INTERFACE.get(),blockPos,blockState);
    }
    public ManaGenericStackInvStorage cachedManaStorage = null;

    public @NotNull ManaGenericStackInvStorage getCachedManaStorage(){
        if (cachedManaStorage==null)
            this.cachedManaStorage = new ManaGenericStackInvStorage(this.getInterfaceLogic().getStorage(),this.level,this.getBlockPos());
        LogUtils.getLogger().info("mana storage max:{} current:{} isfull:{}",cachedManaStorage.getMaxMana(),cachedManaStorage.getCurrentMana(),cachedManaStorage.isFull());
        return cachedManaStorage;
    }


    @Override
    public void onClientDisplayTick() {

    }

    @Override
    public float getManaYieldMultiplier(ManaBurst manaBurst) {
        return 1;
    }

    @Override
    public int getMaxMana() {
        return getCachedManaStorage().getMaxMana();
    }

    @Override
    public Level getManaReceiverLevel() {
        return this.level;
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }

    @Override
    public int getCurrentMana() {
        return getCachedManaStorage().getCurrentMana();
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void receiveMana(int i) {
        getCachedManaStorage().receiveMana(i);
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
