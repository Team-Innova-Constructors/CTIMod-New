package com.hoshino.cti.register;

import com.c2h6s.etshtinker.Entities.damageSources.ThroughSources;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.Cti;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.fluids.block.BurningLiquidBlock;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.hoshino.cti.Cti.MOD_ID;

public class CtiFluid {

    private static FluidType.Properties hot(String name) {
        return FluidType.Properties.create().density(2000).viscosity(10000).temperature(1000).descriptionId(Cti.makeDescriptionId("fluid", name)).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL_LAVA);
    }

    private static FluidType.Properties cool(String name) {
        return cool().descriptionId(Cti.makeDescriptionId("fluid", name)).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL_LAVA);
    }

    private static FluidType.Properties cool() {
        return FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL_LAVA);
    }

    private static FlowingFluidObject<ForgeFlowingFluid> register(String name, int temp) {
        return FLUIDS.register(name).type(hot(name).temperature(temp).lightLevel(12)).block(Material.LAVA, 15).bucket().flowing();
    }

    private static FlowingFluidObject<ForgeFlowingFluid> register(String name, int temp, Function<Supplier<? extends FlowingFluid>, LiquidBlock> liquidBlockConstructor) {
        return FLUIDS.register(name).type(hot(name).temperature(temp).lightLevel(12)).block(liquidBlockConstructor).bucket().flowing();
    }

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(MOD_ID);
    public static final FluidObject<ForgeFlowingFluid> starenergy_fluid = register("starenergy_fluid", 3152);
    public static final FluidObject<ForgeFlowingFluid> molten_uriel = register("molten_uriel", 8192);
    public static final FluidObject<ForgeFlowingFluid> molten_infinity = register("molten_infinity", 8192);
    public static final FluidObject<ForgeFlowingFluid> molten_stellar_manyullyn = register("molten_stellar_manyullyn", 131072,supplier-> new LiquidBlock(supplier, BlockBehaviour.Properties.of(Material.LAVA)){
        @Override
        public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
            if (!(pEntity instanceof Player player&&player.isCreative())){
                pEntity.hurt(EDamageSource.ionize(true,10),Float.MAX_VALUE);
                pEntity.hurt(EDamageSource.scorched(true,10),Float.MAX_VALUE);
                pEntity.hurt(EDamageSource.frozen(true,10),Float.MAX_VALUE);
                pEntity.hurt(EDamageSource.pressure(true,10),Float.MAX_VALUE);
            }
        }
    });
    public static final FluidObject<ForgeFlowingFluid> molten_invert_hoshino = register("molten_invert_hoshino", 350507);
    public static final FluidObject<ForgeFlowingFluid> molten_roxy = register("molten_roxy", 258596);
    public static final FluidObject<ForgeFlowingFluid> molten_omniscient_gold = register("molten_omniscient_gold", 225252);
    public static final FluidObject<ForgeFlowingFluid> molten_violium = register("molten_violium", 6000);
    public static final FluidObject<ForgeFlowingFluid> molten_aetherium = register("molten_aetherium", 7000);
    public static final FluidObject<ForgeFlowingFluid> extraterrestrial_essense = register("extraterrestrial_essense", 0,supplier-> new LiquidBlock(supplier, BlockBehaviour.Properties.of(Material.LAVA)){
        @Override
        public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
            if (!pEntity.noPhysics&&(pEntity instanceof LivingEntity||pEntity instanceof ItemEntity)){
                pEntity.setDeltaMovement(0,100,0);
            }
        }
    });
    public static final FluidObject<ForgeFlowingFluid> molten_meteorium = register("molten_meteorium", 8900);
    public static final FluidObject<ForgeFlowingFluid> molten_awakesaintchef = register("molten_awakesaintchef", 5600);
    public static final FluidObject<ForgeFlowingFluid> molten_bloodgod = register("molten_bloodgod", 1257);
    public static final FluidObject<ForgeFlowingFluid> molten_xinian = register("molten_xinian", 1257);
    public static final FluidObject<ForgeFlowingFluid> MOLTEN_STAR_DRAGON = register("molten_star_dragon", 9273);

    public static final FluidObject<ForgeFlowingFluid> LAVA_HEATED = register("lava_heated", 2000,(supplier)->new BurningLiquidBlock(supplier,BlockBehaviour.Properties.of(Material.LAVA),2000,20));
    public static final FluidObject<ForgeFlowingFluid> LAVA_OVERHEATED = register("lava_overheated", 3500,supplier -> new LiquidBlock(supplier,BlockBehaviour.Properties.of(Material.LAVA)){
        @Override
        public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
            pEntity.invulnerableTime=0;
            pEntity.hurt(DamageSource.LAVA,20);
            pEntity.invulnerableTime=0;
        }
    });
    public static final FluidObject<ForgeFlowingFluid> LAVA_GASEOUS = register("lava_gaseous", 5000,supplier -> new LiquidBlock(supplier,BlockBehaviour.Properties.of(Material.LAVA)){
        @Override
        public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
            pEntity.invulnerableTime=0;
            pEntity.hurt(DamageSource.LAVA.bypassArmor().bypassMagic(),20);
            pEntity.invulnerableTime=0;
        }
    });
    public static final FluidObject<ForgeFlowingFluid> LAVA_PLASMATIC = register("lava_plasmatic", 7000,supplier -> new LiquidBlock(supplier,BlockBehaviour.Properties.of(Material.LAVA)){
        @Override
        public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
            pEntity.invulnerableTime=0;
            pEntity.hurt(EDamageSource.scorched(true,5),25);
            pEntity.invulnerableTime=0;
        }
    });
    public static final FluidObject<ForgeFlowingFluid> LAVA_ATOMIC = register("lava_atomic", 9000,supplier -> new LiquidBlock(supplier,BlockBehaviour.Properties.of(Material.LAVA)){
        @Override
        public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
            if (pEntity instanceof LivingEntity living) {
                pEntity.invulnerableTime = 0;
                ThroughSources.atomic(100).hurtEntity(living);
                pEntity.invulnerableTime = 0;
            }
        }
    });

}
