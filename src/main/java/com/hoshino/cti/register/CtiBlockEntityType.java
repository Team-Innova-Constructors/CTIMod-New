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
}
