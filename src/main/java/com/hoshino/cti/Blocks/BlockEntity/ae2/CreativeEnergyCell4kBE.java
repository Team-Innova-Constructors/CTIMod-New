package com.hoshino.cti.Blocks.BlockEntity.ae2;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.util.AECableType;
import appeng.blockentity.grid.AENetworkBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeEnergyCell4kBE extends AENetworkBlockEntity implements IAEPowerStorage {
    public CreativeEnergyCell4kBE(BlockPos pos, BlockState blockState) {
        super(CtiBlockEntityType.CREATIVE_ENERGY_CELL_4K_BE.get(), pos, blockState);
        this.getMainNode().setIdlePowerUsage((double) 0.0F).addService(IAEPowerStorage.class, this);
    }

    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.COVERED;
    }

    public double injectAEPower(double amt, Actionable mode) {
        return 0.0F;
    }

    public double getAEMaxPower() {
        return 4096;
    }

    public double getAECurrentPower() {
        return 4096;
    }

    public boolean isAEPublicPowerStorage() {
        return true;
    }

    public AccessRestriction getPowerFlow() {
        return AccessRestriction.READ_WRITE;
    }

    public double extractAEPower(double amt, Actionable mode, PowerMultiplier pm) {
        return getAEMaxPower();
    }

    public int getPriority() {
        return Integer.MAX_VALUE;
    }

}

