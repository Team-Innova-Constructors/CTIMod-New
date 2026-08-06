package com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge;

import com.hoshino.cti.Screen.menu.SoulForgeMenu;
import com.hoshino.cti.api.interfaces.IMachineAirHandlerProvider;
import com.hoshino.cti.api.interfaces.IMachineHeatExchangerProvider;
import com.hoshino.cti.netwrok.packet.PAirHandlerSyncS2C;
import com.hoshino.cti.netwrok.packet.PHeatSyncS2C;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.util.CtiEntityMeltingModule;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;

/**
 * 熔魂炉控制器：一个 Tinkers 多方块冶炼炉控制器，熔炼实体时除主产物外还会产生额外产物。
 * <p>
 * 与 PneumaticCraft 联动：
 * <ul>
 *     <li>压力：作为 {@link IMachineAirHandlerProvider} 暴露气罐，绞硅气阀可接入抽真空。
 *     当压力低于 -0.5bar 时实体熔炼基础倍率由 1 变为 4，且每次熔炼实体产出压缩空气。</li>
 *     <li>温度：自身持有热交换器并随炉子内部体积增大热容，
 *     倍率额外随温度降低线性升高（50℃→0℃ 对应 0.5→2 倍），
 *     每次熔炼实体产出热量，可由绞硅导热件向外热交换。</li>
 * </ul>
 * 实体熔炼倍率在每 tick 实时计算，不再依赖结构刷新。
 */
public class SoulForgeControllerBlockEntity extends HeatingStructureBlockEntity implements IMachineAirHandlerProvider, IMachineHeatExchangerProvider {
    private static final int CAPACITY_PER_BLOCK = FluidValues.INGOT * 12;
    private static final int BLOCKS_PER_FUEL = 15;
    private static final Component NAME = Component.translatable("gui.cti.soul_forge");

    /** 真空阈值（bar）：低于此值视为抽真空，启用 4 倍基础熔炼并产出压缩空气 */
    private static final float VACUUM_THRESHOLD = -0.5f;
    /** 温度倍率区间（摄氏度） */
    private static final double TEMP_COLD_C = 0.0;
    private static final double TEMP_WARM_C = 50.0;
    private static final double KELVIN_OFFSET = 273.15;
    /** 温度倍率区间端点 */
    private static final double TEMP_MULT_WARM = 0.5;
    private static final double TEMP_MULT_COLD = 2.0;
    /** 每次熔炼实体产出的压缩空气量（仅在真空时） */
    private static final int AIR_PER_MELT = 10;
    /** 每次熔炼实体产出的热量（任意单位，会按热容折算为温升） */
    private static final double HEAT_PER_MELT = 5.0;
    /** 每方块内部体积对应的额外热容 */
    private static final double HEAT_CAPACITY_PER_BLOCK = 5.0;
    /** 兜底热容，避免容量为 0 导致温度无法累积 */
    private static final double HEAT_CAPACITY_BASE = 10.0;

    /** 当前实体熔炼倍率（每 tick 实时刷新）。默认 1 倍。 */
    private int entityMeltingMultiplier = 1;

    /** 自定义实体熔炼模块，在原版逻辑基础上按倍率放大产物数量。
     *  物品实体的吸取能力被刻意关闭（传一个总是返回输入栈的 no-op 插入函数），
     *  走 ItemEntity 分支时只会丢弃空栈而不会把掉落物塞进熔炼物品栏。 */
    protected final CtiEntityMeltingModule ctiEntityModule =
            new CtiEntityMeltingModule(this, tank, this::canMeltEntities, () -> structure == null ? null : structure.getBounds(), () -> entityMeltingMultiplier);

    /** 气罐：绞硅气阀通过 {@link IMachineAirHandlerProvider} 包装并向外暴露。 */
    protected final MachineAirHandler machineAirHandler = new MachineAirHandler(PressureTier.TIER_ONE_HALF, 5000) {
        @Override
        public @Nullable Direction getSideLeaking() {
            return null;
        }
    };

    /** 热交换器：存储温度，随炉子体积增大热容，并向外通过导热件热交换。 */
    protected final IHeatExchangerLogic heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
    private final LazyOptional<IHeatExchangerLogic> heatCap = LazyOptional.of(() -> heatExchanger);

    public SoulForgeControllerBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.SOUL_FORGE.get(), pos, state, NAME);
        heatExchanger.setThermalCapacity(HEAT_CAPACITY_BASE);
    }

    @Override
    protected HeatingStructureMultiblock<?> createMultiblock() {
        return new SoulForgeMultiblock(this);
    }

    @Override
    protected MeltingModuleInventory createMeltingInventory() {
        return new MeltingModuleInventory(this, tank, Config.COMMON.smelteryOreRate);
    }

    @Override
    protected boolean isDebugItem(ItemStack stack) {
        return false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            heatExchanger.initializeAmbientTemperature(level, worldPosition);
        }
    }

    @Override
    protected void serverTick(Level level, BlockPos pos, BlockState state) {
        // 先按当前压力/温度刷新实体熔炼倍率，确保本轮 heat() 用到的倍率是最新的
        updateEntityMeltingMultiplier();
        super.serverTick(level, pos, state);
        // 驱动气动与热交换系统的 tick
        machineAirHandler.tick(this);
        heatExchanger.tick();
        if (level.getGameTime() % 10 == 0) {
            PAirHandlerSyncS2C.syncAirToClient(this);
            PHeatSyncS2C.syncHeatToClient(this);
        }
    }

    /** 依据当前压力与温度实时计算实体熔炼倍率 */
    private void updateEntityMeltingMultiplier() {
        float pressure = machineAirHandler.getPressure();
        int base = pressure < VACUUM_THRESHOLD ? 4 : 1;
        double tempK = heatExchanger.getTemperature();
        double tempC = tempK - KELVIN_OFFSET;
        double clampedC = Mth.clamp(tempC, TEMP_COLD_C, TEMP_WARM_C);
        double frac = (TEMP_WARM_C - clampedC) / (TEMP_WARM_C - TEMP_COLD_C);
        double tempMult = TEMP_MULT_WARM + frac * (TEMP_MULT_COLD - TEMP_MULT_WARM);
        entityMeltingMultiplier = Math.max(1, (int) Math.round(base * tempMult));
    }

    @Override
    protected void heat() {
        if (structure == null || level == null) {
            return;
        }
        if (structure.hasTanks()) {
            int meltedCount = 0;
            if (tick == 12) {
                meltedCount = ctiEntityModule.interactWithEntities(fuelModule.getTemperature());
            }
            // 熔炼副作用：真空产出压缩空气、每次熔炼产出热量
            if (meltedCount > 0) {
                if (machineAirHandler.getPressure() < VACUUM_THRESHOLD) {
                    machineAirHandler.addAir(AIR_PER_MELT * meltedCount);
                }
                heatExchanger.addHeat(HEAT_PER_MELT * meltedCount);
            }
            // 熔魂炉不熔炼物品，轮询熔炼物品库存的分支被移除
            switch (tick % 4) {
                case 0:
                    // 仅在熔炼了实体时才查找/消耗燃料（凭熔炼实体产生的热量维持炉温）
                    if (meltedCount > 0 && !fuelModule.hasFuel()) {
                        fuelModule.findFuel(true);
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
            int size = dx * dy * dz;
            tank.setCapacity(CAPACITY_PER_BLOCK * size);
            meltingInventory.resize(size, dropItem);
            fuelRate = 1 + ((2 * (dx * dy) + 2 * (dy * dz) + (dx * dz))) / BLOCKS_PER_FUEL;
            //热容随炉子内部体积增加
            heatExchanger.setThermalCapacity(HEAT_CAPACITY_BASE + Math.max(0, size) * HEAT_CAPACITY_PER_BLOCK);
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new SoulForgeMenu(id, inv, (HeatingStructureBlockEntity) this);
    }

    @Override
    public MachineAirHandler getAirHandler() {
        return machineAirHandler;
    }

    public IHeatExchangerLogic getHeatExchanger() {
        return heatExchanger;
    }

    public float getPressure() {
        return machineAirHandler.getPressure();
    }

    public double getTemperature() {
        return heatExchanger.getTemperature();
    }

    /** 当前实体熔炼倍率，供 GUI/Waila 显示 */
    public int getEntityMeltingMultiplier() {
        return entityMeltingMultiplier;
    }

    /** 检查是否具备熔炼实体的条件（有燃料或可找到燃料） */
    private boolean canMeltEntities() {
        if (fuelModule.hasFuel()) {
            return true;
        }
        return fuelModule.findFuel(false) > 0;
    }

    @Override
    public void saveSynced(CompoundTag compound) {
        super.saveSynced(compound);
        compound.put("air", machineAirHandler.serializeNBT());
        compound.put("heat", heatExchanger.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        machineAirHandler.deserializeNBT(nbt.getCompound("air"));
        heatExchanger.deserializeNBT(nbt.getCompound("heat"));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        // 熔魂炉不持有任何可被外部插入/抽取的物品存储：屏蔽父类暴露的 ITEM_HANDLER 能力，
        // 避免漏斗/滑槽/管道等继续通过它给熔炼物品栏塞东西。
        if (capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.empty();
        }
        if (capability == PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY) {
            return LazyOptional.of(() -> machineAirHandler).cast();
        }
        if (capability == PNCCapabilities.HEAT_EXCHANGER_CAPABILITY) {
            return heatCap.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        heatCap.invalidate();
    }
}