package com.hoshino.cti.register;

import com.hoshino.cti.Blocks.*;
import com.hoshino.cti.Blocks.BlockEntity.AdvancedAlloyerBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.HepatizonCastingBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.ZirconiumCastingBlockEntity;
import com.hoshino.cti.Blocks.Machine.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.CastingBasinBlock;
import slimeknights.tconstruct.smeltery.block.CastingTableBlock;
import slimeknights.tconstruct.smeltery.block.controller.AlloyerBlock;
import slimeknights.tconstruct.world.block.CrystalClusterBlock;

import java.util.List;


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
    public static final RegistryObject<Block> qi_yao_matrix = BLOCK.register("qi_yao_matrix", () -> new Block(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_BLUE)
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
            return 7;
        }
    });
    public static final RegistryObject<BaseEntityBlock> atmosphere_extractor = BLOCK.register("atmosphere_extractor", () -> new AtmosphereExtractorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> atmosphere_condensator = BLOCK.register("atmosphere_condensator", () -> new AtmosphereCondensatorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> quantum_miner = BLOCK.register("quantum_miner", () -> new QuantumMinerBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> quantum_miner_advanced = BLOCK.register("quantum_miner_advanced", () -> new QuantumMinerAdvancdBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> reactor_neutron_collector = BLOCK.register("reactor_neutron_collector", () -> new ReactorNeutronCollectorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> alloy_centrifuge_block = BLOCK.register("alloy_centrifuge", () -> new AlloyCentrifugeBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));
    public static final RegistryObject<BaseEntityBlock> sodium_cooler_block = BLOCK.register("sodium_cooler", () -> new SodiumCooler(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).destroyTime(2).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> HEPATIZON_FAUCET = BLOCK.register("hepatizon_faucet", () ->
            new HepatizonFaucetBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.searedFaucet.get()).sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> HEPATIZON_TABLE = BLOCK.register("hepatizon_casting_table", () ->
            new CastingTableBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.searedTable.get())
                    .sound(SoundType.NETHERITE_BLOCK),true) {
                @Override
                public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
                    return List.of( new ItemStack(this.asItem()));
                }
                @Override
                public @NotNull BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
                    return new HepatizonCastingBlockEntity.Table(pPos,pState);
                }

                @Override
                public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> check) {
                    return HepatizonCastingBlockEntity.getTicker(pLevel,check, CtiBlockEntityType.HEPATIZON_TABLE.get());
                }
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.cti.hepatizon_casting",4).withStyle(ChatFormatting.GRAY));
                }
            });
    public static final RegistryObject<Block> HEPATIZON_BASIN = BLOCK.register("hepatizon_casting_basin", () ->
            new CastingBasinBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.searedTable.get())
                    .sound(SoundType.NETHERITE_BLOCK),true) {
                @Override
                public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
                    return List.of( new ItemStack(this.asItem()));
                }
                @Override
                public @NotNull BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
                    return new HepatizonCastingBlockEntity.Basin(pPos,pState);
                }

                @Override
                public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> check) {
                    return HepatizonCastingBlockEntity.getTicker(pLevel,check, CtiBlockEntityType.HEPATIZON_BASIN.get());
                }
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.cti.hepatizon_casting",4).withStyle(ChatFormatting.GRAY));
                }
            });


    public static final RegistryObject<Block> ZR_ALLOY_FAUCET = BLOCK.register("zirconium_alloy_faucet", () ->
            new ZirconiumFaucetBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.searedFaucet.get()).sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> ZR_ALLOY_TABLE = BLOCK.register("zirconium_alloy_casting_table", () ->
            new ZirconiumTableBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.searedTable.get())
                    .sound(SoundType.NETHERITE_BLOCK),true) {
                @Override
                public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
                    return List.of( new ItemStack(this.asItem()));
                }
                @Override
                public @NotNull BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
                    return new ZirconiumCastingBlockEntity.Table(pPos,pState);
                }

                @Override
                public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> check) {
                    return ZirconiumCastingBlockEntity.getTicker(pLevel,check, CtiBlockEntityType.ZR_ALLOY_TABLE.get());
                }
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.cti.zirconium_alloy_casting").withStyle(ChatFormatting.GRAY));
                    pTooltip.add(Component.translatable("tooltip.cti.zirconium_alloy_casting2").withStyle(ChatFormatting.GRAY));
                    pTooltip.add(Component.translatable("tooltip.cti.zirconium_alloy_casting3").withStyle(ChatFormatting.DARK_AQUA));
                }
            });
    public static final RegistryObject<Block> ZR_ALLOY_BASIN = BLOCK.register("zirconium_alloy_casting_basin", () ->
            new ZirconiumBasinBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.searedTable.get())
                    .sound(SoundType.NETHERITE_BLOCK),true) {
                @Override
                public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
                    return List.of( new ItemStack(this.asItem()));
                }
                @Override
                public @NotNull BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
                    return new ZirconiumCastingBlockEntity.Basin(pPos,pState);
                }

                @Override
                public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> check) {
                    return ZirconiumCastingBlockEntity.getTicker(pLevel,check, CtiBlockEntityType.ZR_ALLOY_BASIN.get());
                }
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.cti.zirconium_alloy_casting").withStyle(ChatFormatting.GRAY));
                    pTooltip.add(Component.translatable("tooltip.cti.zirconium_alloy_casting2").withStyle(ChatFormatting.GRAY));
                    pTooltip.add(Component.translatable("tooltip.cti.zirconium_alloy_casting3").withStyle(ChatFormatting.DARK_AQUA));
                }
            });


    public static final RegistryObject<Block> ADVANCED_ALLOYER = BLOCK.register("advanced_alloyer", () ->
            new AlloyerBlock(BlockBehaviour.Properties.copy(TinkerSmeltery.scorchedFaucet.get()).sound(SoundType.NETHERITE_BLOCK)){
                @Override
                public @NotNull BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
                    return new AdvancedAlloyerBlockEntity(pPos,pState);
                }
                @Override
                public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> wanted) {
                    return AdvancedAlloyerBlockEntity.getTicker(pLevel,wanted,CtiBlockEntityType.ADVANCED_ALLOYER.get());
                }

                @Override
                public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
                    return List.of(new ItemStack(this.asItem()));
                }

                @Override
                public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> list, TooltipFlag p_49819_) {
                    list.add(Component.translatable("tooltip.cti.advanced_alloyer1")
                            .withStyle(ChatFormatting.GRAY));
                    list.add(Component.translatable("tooltip.cti.advanced_alloyer2")
                            .withStyle(ChatFormatting.GRAY));
                }
            });
}
