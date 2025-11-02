package com.hoshino.cti.register;

import com.hoshino.cti.Blocks.FractureSiliconBudding;
import com.hoshino.cti.Blocks.Machine.*;
import com.hoshino.cti.Blocks.OverdenseGlacioStone;
import com.hoshino.cti.Blocks.RasteriteBudding;
import com.hoshino.cti.Blocks.unipolarBudding;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.world.block.CrystalClusterBlock;


public class CtiBlock {
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, "cti");
    public static final RegistryObject<Block> unipolar_magnet_budding = BLOCK.register("unipolar_magnet_budding", () -> new unipolarBudding(BlockBehaviour.Properties.of(Material.AMETHYST).lightLevel((BlockStateBase) -> 15).sound(SoundType.AMETHYST).randomTicks().destroyTime(1)));
    public static final RegistryObject<Block> unipolar_magnet = BLOCK.register("unipolar_magnet", () -> new CrystalClusterBlock(SoundEvents.AMETHYST_BLOCK_CHIME, 7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().sound(SoundType.AMETHYST_CLUSTER).strength(5.5F).lightLevel((BlockStateBase) -> 5).destroyTime(1)));
    public static final RegistryObject<Block> overdense_glacio_stone = BLOCK.register("overdense_glacio_stone", () -> new OverdenseGlacioStone(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.ANCIENT_DEBRIS).randomTicks().destroyTime(3)));
    public static final RegistryObject<Block> ultra_dense_hydride_ore = BLOCK.register("ultra_dense_hydride_ore", () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST).sound(SoundType.AMETHYST).lightLevel((BlockStateBase) -> 10)));
    public static final RegistryObject<Block> fracture_silicon = BLOCK.register("fracture_silicon", () -> new CrystalClusterBlock(SoundEvents.AMETHYST_BLOCK_CHIME, 7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().sound(SoundType.AMETHYST_CLUSTER).strength(2.5F).lightLevel((BlockStateBase) -> 5).destroyTime(1)));
    public static final RegistryObject<Block> rasterite = BLOCK.register("rasterite", () -> new CrystalClusterBlock(SoundEvents.AMETHYST_BLOCK_CHIME, 7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().sound(SoundType.AMETHYST_CLUSTER).strength(2.5F).lightLevel((BlockStateBase) -> 5).destroyTime(1)));
    public static final RegistryObject<Block> rasterite_budding = BLOCK.register("rasterite_budding", () -> new RasteriteBudding(BlockBehaviour.Properties.of(Material.AMETHYST).lightLevel((BlockStateBase) -> 5).sound(SoundType.AMETHYST).randomTicks()));
    public static final RegistryObject<Block> fracture_silicon_budding = BLOCK.register("fracture_silicon_budding", () -> new FractureSiliconBudding(BlockBehaviour.Properties.of(Material.AMETHYST).lightLevel((BlockStateBase) -> 15).sound(SoundType.AMETHYST).randomTicks()));
    public static final RegistryObject<Block> meteorite_ore = BLOCK.register("meteorite_ore", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.ANCIENT_DEBRIS).lightLevel((BlockStateBase) -> 3)));
    public static final RegistryObject<GlassBlock> aluminium_glass = BLOCK.register("aluminium_glass", () -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS,MaterialColor.COLOR_YELLOW)
            .sound(SoundType.GLASS).strength(5)
            .noOcclusion()
            .isValidSpawn((a,b,c,d)->false)
            .lightLevel((a)->0)
            .isViewBlocking((a,b,c)->false)
            .isSuffocating((a,b,c)->false)
            .isRedstoneConductor((a,b,c)->true)
    ){
        @Override
        public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
            return 350234;
        }
    });
    public static final RegistryObject<BaseEntityBlock> atmosphere_extractor = BLOCK.register("atmosphere_extractor", () -> new AtmosphereExtractorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> atmosphere_condensator = BLOCK.register("atmosphere_condensator", () -> new AtmosphereCondensatorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> quantum_miner = BLOCK.register("quantum_miner", () -> new QuantumMinerBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> quantum_miner_advanced = BLOCK.register("quantum_miner_advanced", () -> new QuantumMinerAdvancdBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> reactor_neutron_collector = BLOCK.register("reactor_neutron_collector", () -> new ReactorNeutronCollectorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> alloy_centrifuge_block = BLOCK.register("alloy_centrifuge", () -> new AlloyCentrifugeBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> sodium_cooler_block = BLOCK.register("sodium_cooler", () -> new SodiumCooler(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
}
