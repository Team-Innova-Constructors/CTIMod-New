package com.hoshino.cti.register;

import appeng.api.config.Actionable;
import com.hoshino.cti.Blocks.BlockEntity.*;
import com.hoshino.cti.Blocks.BlockEntity.ae2.CreativeEnergyCell4kBE;
import com.hoshino.cti.Blocks.BlockEntity.botania.ReactiveFLowerBE;
import com.hoshino.cti.Blocks.BlockEntity.mek.FissionNanoFixPortBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.*;
import com.hoshino.cti.Blocks.BlockEntity.tinker.refinery.RefineryControllerBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge.SoulBrickHeatConductorBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge.SoulForgeControllerBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge.SoulValveBlockEntity;
import com.hoshino.cti.Cti;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.registries.GeneratorsContainerTypes;
import mekanism.generators.common.registries.GeneratorsSounds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock;

import java.util.function.Supplier;

public class CtiBlockEntityType {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Cti.MOD_ID);

    public static final RegistryObject<BlockEntityType<AtmosphereExtractorEntity>> Atmosphere_extractor = BLOCK_ENTITIES.register("atmosphere_extractor", () -> BlockEntityType.Builder.of(AtmosphereExtractorEntity::new, CtiBlock.atmosphere_extractor.get()).build(null));
    public static final RegistryObject<BlockEntityType<AtmosphereCondensatorEntity>> Atmosphere_condensator = BLOCK_ENTITIES.register("atmosphere_condensator", () -> BlockEntityType.Builder.of(AtmosphereCondensatorEntity::new, CtiBlock.atmosphere_condensator.get()).build(null));
    public static final RegistryObject<BlockEntityType<QuantumMinerEntity>> QUANTUM_MINER_ENTITY = BLOCK_ENTITIES.register("quantum_miner", () -> BlockEntityType.Builder.of(QuantumMinerEntity::new, CtiBlock.quantum_miner.get()).build(null));
    public static final RegistryObject<BlockEntityType<QuantumMinerAdvancedEntity>> QUANTUM_MINER_ADVANCED_ENTITY = BLOCK_ENTITIES.register("quantum_miner_advanced", () -> BlockEntityType.Builder.of(QuantumMinerAdvancedEntity::new, CtiBlock.quantum_miner_advanced.get()).build(null));
    public static final RegistryObject<BlockEntityType<ReactorNeutronCollectorEntity>> REACTOR_NEUTRON_COLLECTOR = BLOCK_ENTITIES.register("reactor_neutron_collector", () -> BlockEntityType.Builder.of(ReactorNeutronCollectorEntity::new, CtiBlock.reactor_neutron_collector.get()).build(null));
    public static final RegistryObject<BlockEntityType<AlloyCentrifugeEntity>> ALLOY_CENTRIFUGE = BLOCK_ENTITIES.register("alloy_centrifuge", () -> BlockEntityType.Builder.of(AlloyCentrifugeEntity::new, CtiBlock.alloy_centrifuge_block.get()).build(null));
    public static final RegistryObject<BlockEntityType<SodiumCoolerEntity>> SODIUM_COOLER = BLOCK_ENTITIES.register("sodium_cooler", () -> BlockEntityType.Builder.of(SodiumCoolerEntity::new, CtiBlock.sodium_cooler_block.get()).build(null));


    public static final RegistryObject<BlockEntityType<HepatizonCastingBlockEntity.Table>> HEPATIZON_TABLE =
            BLOCK_ENTITIES.register("hepatizon_casting_table_be",()->
                    BlockEntityType.Builder.of(HepatizonCastingBlockEntity.Table::new, CtiBlock.HEPATIZON_TABLE.get())
                            .build(null));
    public static final RegistryObject<BlockEntityType<HepatizonCastingBlockEntity.Basin>> HEPATIZON_BASIN =
            BLOCK_ENTITIES.register("hepatizon_casting_basin_be",()->
                    BlockEntityType.Builder.of(HepatizonCastingBlockEntity.Basin::new, CtiBlock.HEPATIZON_BASIN.get())
                            .build(null));
    public static final RegistryObject<BlockEntityType<HepatizonFaucetBlockEntity>> HEPATIZON_FAUCET =
            BLOCK_ENTITIES.register("hepatizon_faucet_be",()->
                    BlockEntityType.Builder.of(HepatizonFaucetBlockEntity::new, CtiBlock.HEPATIZON_FAUCET.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ZirconiumCastingBlockEntity.Table>> ZR_ALLOY_TABLE =
            BLOCK_ENTITIES.register("zirconium_alloy_table_be",()->
                    BlockEntityType.Builder.of(ZirconiumCastingBlockEntity.Table::new, CtiBlock.ZR_ALLOY_TABLE.get())
                            .build(null));
    public static final RegistryObject<BlockEntityType<ZirconiumCastingBlockEntity.Basin>> ZR_ALLOY_BASIN =
            BLOCK_ENTITIES.register("zirconium_alloy_basin_be",()->
                    BlockEntityType.Builder.of(ZirconiumCastingBlockEntity.Basin::new, CtiBlock.ZR_ALLOY_BASIN.get())
                            .build(null));
    public static final RegistryObject<BlockEntityType<ZirconiumFaucetBlockEntity>> ZR_ALLOY_FAUCET =
            BLOCK_ENTITIES.register("zirconium_alloy_faucet_be",()->
                    BlockEntityType.Builder.of(ZirconiumFaucetBlockEntity::new, CtiBlock.ZR_ALLOY_FAUCET.get())
                            .build(null));


    public static final RegistryObject<BlockEntityType<AdvancedAlloyerBlockEntity>> ADVANCED_ALLOYER =
            BLOCK_ENTITIES.register("advanced_alloyer_be",()->
                    BlockEntityType.Builder.of(AdvancedAlloyerBlockEntity::new, CtiBlock.ADVANCED_ALLOYER.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<RefineryControllerBlockEntity>> REFINERY =
            BLOCK_ENTITIES.register("refinery_be",()->
                    BlockEntityType.Builder.of(RefineryControllerBlockEntity::new, CtiBlock.REFINERY.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<SoulForgeControllerBlockEntity>> SOUL_FORGE =
            BLOCK_ENTITIES.register("soul_forge_be",()->
                    BlockEntityType.Builder.of(SoulForgeControllerBlockEntity::new, CtiBlock.SOUL_FORGE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<SoulBrickHeatConductorBlockEntity>> SOUL_BRICK_HEAT_CONDUCTOR =
            BLOCK_ENTITIES.register("soul_brick_heat_conductor_be", () ->
                    BlockEntityType.Builder.of(SoulBrickHeatConductorBlockEntity::new,
                                    CtiBlock.SOUL_BRICK_HEAT_CONDUCTOR.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<SoulValveBlockEntity>> SOUL_VALVE =
            BLOCK_ENTITIES.register("soul_valve", () ->
                    BlockEntityType.Builder.of(SoulValveBlockEntity::new,
                                    CtiBlock.SOUL_VALVE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<CtiTankBlockEntity>> TANK =
            BLOCK_ENTITIES.register("tank",()->
                    BlockEntityType.Builder.of(CtiTankBlockEntity::new, CtiBlock.SILICATED_TANK.get(SearedTankBlock.TankType.FUEL_GAUGE),
                                    CtiBlock.SILICATED_TANK.get(SearedTankBlock.TankType.INGOT_GAUGE),
                                    CtiBlock.SOUL_FORGE_TANK.get(SearedTankBlock.TankType.FUEL_GAUGE),
                                    CtiBlock.SOUL_FORGE_TANK.get(SearedTankBlock.TankType.INGOT_GAUGE))
                            .build(null));
    public static final RegistryObject<BlockEntityType<ValveBlockEntity>> VAULT =
            BLOCK_ENTITIES.register("vault",()->
                    BlockEntityType.Builder.of(ValveBlockEntity::new, CtiBlock.SILICATED_VALVE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ManaInterfaceBE>> MANA_INTERFACE =
            BLOCK_ENTITIES.register("mana_interface",()-> {
                        var type = BlockEntityType.Builder.of(ManaInterfaceBE::new, CtiBlock.MANA_INTERFACE.get())
                                .build(null);
                        CtiBlock.MANA_INTERFACE.get().setBlockEntity(ManaInterfaceBE.class, type, null, null);
                        return type;
                    });

    public static final RegistryObject<BlockEntityType<CreativeEnergyCell4kBE>> CREATIVE_ENERGY_CELL_4K_BE =
            BLOCK_ENTITIES.register("creative_energy_cell_4k",()-> {
                var type = BlockEntityType.Builder.of(CreativeEnergyCell4kBE::new, CtiBlock.CREATIVE_ENERGY_CELL_4K.get())
                        .build(null);
                CtiBlock.CREATIVE_ENERGY_CELL_4K.get().setBlockEntity(CreativeEnergyCell4kBE.class, type, null, (pLevel, pPos, pState, pBlockEntity) ->
                {
                    var grid = pBlockEntity.getGridNode();
                    if (grid!=null&&grid.getGrid()!=null&&grid.getGrid().getEnergyService()!=null){
                        grid.getGrid().getEnergyService().injectPower(pBlockEntity.getAEMaxPower(), Actionable.MODULATE);
                    }
                });
                return type;
            });

    public static final RegistryObject<BlockEntityType<ReactiveFLowerBE>> REACTIVE_FLOWER =
            BLOCK_ENTITIES.register("reactive_flower",()-> BlockEntityType.Builder.of(ReactiveFLowerBE::new, CtiBlock.REACTIVE_FLOWER.get(),CtiBlock.FLOATING_REACTIVE_FLOWER.get())
                    .build(null));
    public static class MekBE{
        public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Cti.MOD_ID);
        public static final TileEntityTypeRegistryObject<FissionNanoFixPortBlockEntity> FISSION_NANO_FIX_PORT = TILE_ENTITY_TYPES.register(MekBlock.FISSION_NANO_FIX_PORT_BLOCK, FissionNanoFixPortBlockEntity::new);

    }
    public static class MekBlock{
        public static final BlockDeferredRegister MEKANISM_BLOCKS = new BlockDeferredRegister(Cti.MOD_ID);
        private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerTooltipBlock(String name, Supplier<BLOCK> blockCreator) {
            return MEKANISM_BLOCKS.registerDefaultProperties(name, blockCreator, ItemBlockTooltip::new);
        }
        public static final BlockRegistryObject<BlockBasicMultiblock<FissionNanoFixPortBlockEntity>,ItemBlockTooltip<BlockBasicMultiblock<FissionNanoFixPortBlockEntity>>> FISSION_NANO_FIX_PORT_BLOCK = registerTooltipBlock("fission_nano_fix_port",()->new BlockBasicMultiblock<>(MekBEType.FISSION_NANO_FIX_PORT_BE_TYPE));
    }
    public static class MekBEType{
        public static final BlockTypeTile<FissionNanoFixPortBlockEntity> FISSION_NANO_FIX_PORT_BE_TYPE = BlockTypeTile.BlockTileBuilder
                .createBlock(() -> MekBE.FISSION_NANO_FIX_PORT, () -> "cti.tooltip.fission_nano_fix_port")
                .withGui(() -> GeneratorsContainerTypes.FISSION_REACTOR, GeneratorsLang.FISSION_REACTOR)
                .withSound(GeneratorsSounds.FISSION_REACTOR)
                .externalMultiblock()
                .build();
    }



}
