package com.hoshino.cti.Blocks.BlockEntity.tinker;

import com.hoshino.cti.Blocks.BlockEntity.tinker.refinery.RefineryControllerBlockEntity;
import com.hoshino.cti.api.interfaces.IMachineAirHandlerProvider;
import com.hoshino.cti.register.CtiBlockEntityType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import me.desht.pneumaticcraft.common.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity;

import java.util.*;
import java.util.function.Predicate;

public class ValveBlockEntity extends SmelteryInputOutputBlockEntity<IAirHandlerMachine> {
    public ValveBlockEntity(BlockPos pos, BlockState state){
        this(CtiBlockEntityType.VAULT.get(),pos,state);
    }
    @javax.annotation.Nullable
    public static <CAST extends ValveBlockEntity, RET extends BlockEntity> BlockEntityTicker<RET> getTicker(Level level, BlockEntityType<RET> check, BlockEntityType<CAST> casting) {
        if (level.isClientSide) return null;
        return BlockEntityHelper.castTicker(check, casting, SERVER_TICKER);
    }

    private final Map<IAirHandlerMachine, List<Direction>> airHandlerMap = new IdentityHashMap();
    private LazyOptional<IAirHandlerMachine> airHandler = LazyOptional.empty();
    protected ValveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, new IAirHandlerMachine() {
            @Override
            public float getDangerPressure() {
                return 0;
            }
            @Override
            public float getCriticalPressure() {
                return 0;
            }
            @Override
            public void setPressure(float v) {}
            @Override
            public void setVolumeUpgrades(int i) {}
            @Override
            public void enableSafetyVenting(Predicate<Float> predicate, Direction direction) {}
            @Override
            public void disableSafetyVenting() {}
            @Override
            public void tick(BlockEntity blockEntity) {}
            @Override
            public void setSideLeaking(@Nullable Direction direction) {}
            @Override
            public @Nullable Direction getSideLeaking() {
                return null;
            }
            @Override
            public List<Connection> getConnectedAirHandlers(BlockEntity blockEntity) {
                return List.of();
            }
            @Override
            public void setConnectedFaces(List<Direction> list) {}
            @Override
            public float getPressure() {
                return 0;
            }
            @Override
            public int getAir() {
                return 0;
            }
            @Override
            public void addAir(int i) {}
            @Override
            public int getBaseVolume() {
                return 0;
            }
            @Override
            public void setBaseVolume(int i) {}
            @Override
            public int getVolume() {
                return 0;
            }
            @Override
            public float maxPressure() {
                return 0;
            }
            @Override
            public void printManometerMessage(Player player, List<Component> list) {}
            @Override
            public CompoundTag serializeNBT() {
                return new CompoundTag();
            }
            @Override
            public void deserializeNBT(CompoundTag nbt) {}
        });
    }
    public static BlockEntityTicker<ValveBlockEntity> SERVER_TICKER = (level,pos,state,blockEntity)->blockEntity.serverTick(level,pos,state,blockEntity);

    protected void serverTick(Level level, BlockPos pos, BlockState state,ValveBlockEntity blockEntity){
        airHandler.ifPresent(handler->handler.tick(blockEntity));
    }

    @Override
    protected void setMaster(@Nullable BlockPos master, @Nullable Block block) {
        super.setMaster(master, block);
        assert level!=null;
        boolean masterChanged = !Objects.equals(getMasterPos(), master);
        if (masterChanged) this.airHandler = LazyOptional.empty();
        if (master!=null) {
            this.initializeHullAirHandlers();
            level.blockUpdated(worldPosition, getBlockState().getBlock());
        }
    }

    @Override
    public void onLoad() {
        this.initializeHullAirHandlers();
        super.onLoad();
    }

    @Override
    protected LazyOptional<IAirHandlerMachine> getCapability(BlockEntity parent) {
        if (!this.airHandler.isPresent()&&parent instanceof IMachineAirHandlerProvider provider)
            this.airHandler = LazyOptional.of(()->new VaultWrappedMachineAirHandler(provider));
        return LazyOptional.of(()->this.airHandler.orElse(emptyInstance));
    }

    public void initializeHullAirHandlers() {
        this.airHandlerMap.clear();

        for(Direction side : DirectionUtil.VALUES) {
            this.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, side).ifPresent((handler) -> (this.airHandlerMap.computeIfAbsent(handler, (k) -> new ArrayList<>())).add(side));
        }

        this.airHandlerMap.forEach(IAirHandlerMachine::setConnectedFaces);
    }

    public static class VaultWrappedMachineAirHandler extends MachineAirHandler {
        protected final IMachineAirHandlerProvider airHandler;
        public VaultWrappedMachineAirHandler(IMachineAirHandlerProvider parent) {
            super(new PressureTier() {
                @Override
                public float getDangerPressure() {
                    return parent.getAirHandler().getDangerPressure();
                }

                @Override
                public float getCriticalPressure() {
                    return parent.getAirHandler().getCriticalPressure();
                }
            }, parent.getAirHandler().getVolume());
            this.airHandler = parent;
        }

        @Override
        public float getDangerPressure() {
            return airHandler.getAirHandler().getDangerPressure();
        }

        @Override
        public float getCriticalPressure() {
            return airHandler.getAirHandler().getCriticalPressure();
        }

        @Override
        public void setPressure(float v) {
            airHandler.getAirHandler().setPressure(v);
        }

        @Override
        public void setVolumeUpgrades(int i) {
            airHandler.getAirHandler().setVolumeUpgrades(i);
        }

        @Override
        public @Nullable Direction getSideLeaking() {
            return null;
        }

        @Override
        public float getPressure() {
            return airHandler.getAirHandler().getPressure();
        }
        @Override
        public int getAir() {
            return airHandler.getAirHandler().getAir();
        }
        @Override
        public void addAir(int i) {
            airHandler.getAirHandler().addAir(i);
        }
        @Override
        public int getBaseVolume() {
            return airHandler.getAirHandler().getBaseVolume();
        }
        @Override
        public void setBaseVolume(int i) {
            airHandler.getAirHandler().setBaseVolume(i);
        }
        @Override
        public int getVolume() {
            return airHandler.getAirHandler().getVolume();
        }
        @Override
        public float maxPressure() {
            return airHandler.getAirHandler().getPressure();
        }

        @Override
        public CompoundTag serializeNBT() {
            return new CompoundTag();
        }
        @Override
        public void deserializeNBT(CompoundTag nbt) {

        }
    }
}
