package com.hoshino.cti.register;

import com.hoshino.cti.Blocks.BlockEntity.*;
import com.hoshino.cti.Cti;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

}
