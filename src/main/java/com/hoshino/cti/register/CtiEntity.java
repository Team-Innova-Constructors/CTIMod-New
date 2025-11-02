package com.hoshino.cti.register;

import com.bobmowzie.mowziesmobs.client.render.entity.RenderSunstrike;
import com.c2h6s.etshtinker.client.render.renderSlash;
import com.hoshino.cti.Entity.Projectiles.*;
import com.hoshino.cti.Entity.vehicles.rocketTier5;
import com.hoshino.cti.client.renderer.projectile.*;
import com.hoshino.cti.client.renderer.vehicle.rocketTier5.RocketRendererTier5;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.botarium.client.ClientHooks;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;

public class CtiEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "cti");
    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister("cti");

    public static final ResourcefulRegistry<EntityType<?>> ENTITY_TYPES = ResourcefulRegistries.create(Registry.ENTITY_TYPE, "cti");
    public static final RegistryEntry<EntityType<rocketTier5>> TIER_5_ROCKET = ENTITY_TYPES.register("tier_5_rocket", () -> EntityType.Builder.<rocketTier5>of(rocketTier5::new, MobCategory.MISC).sized(1.1f, 7.0f).fireImmune().build("cti"));
    public static final RegistryObject<EntityType<FallenStars>> star_blaze = ENTITIES.register("star_blaze", () -> EntityType.Builder.<FallenStars>of((entityType, level) -> new FallenStars(entityType, level, CtiItem.star_blaze.get()), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new FallenStars(CtiEntity.star_blaze.get(), world, CtiItem.star_blaze.get())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<FallenStars>> star_ionize = ENTITIES.register("star_ionize", () -> EntityType.Builder.<FallenStars>of((entityType, level) -> new FallenStars(entityType, level, CtiItem.star_ionize.get()), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new FallenStars(CtiEntity.star_ionize.get(), world, CtiItem.star_ionize.get())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<FallenStars>> star_frozen = ENTITIES.register("star_frozen", () -> EntityType.Builder.<FallenStars>of((entityType, level) -> new FallenStars(entityType, level, CtiItem.star_frozen.get()), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new FallenStars(CtiEntity.star_frozen.get(), world, CtiItem.star_frozen.get())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<FallenStars>> star_pressure = ENTITIES.register("star_pressure", () -> EntityType.Builder.<FallenStars>of((entityType, level) -> new FallenStars(entityType, level, CtiItem.star_pressure.get()), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new FallenStars(CtiEntity.star_pressure.get(), world, CtiItem.star_pressure.get())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<TinkerRailgunProjectile>> tinker_railgun = ENTITIES.register("tinker_railgun", () -> EntityType.Builder.<TinkerRailgunProjectile>of((entityType, level) -> new TinkerRailgunProjectile(entityType, level, new ItemStack(TinkerToolParts.toolHandle), TinkerTools.cleaver.get()), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new TinkerRailgunProjectile(CtiEntity.tinker_railgun.get(), world, new ItemStack(TinkerToolParts.toolHandle), TinkerTools.cleaver.get())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<MeteorEntity>> meteor_entity = ENTITIES.register("meteor_entity", () -> EntityType.Builder.<MeteorEntity>of(MeteorEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new MeteorEntity(world, spawnEntity.getPosX(), spawnEntity.getPosY(), spawnEntity.getPosZ(), spawnEntity.getEntity().getDeltaMovement())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<FriendlyMeteor>> FRIENDLY_METEOR = ENTITIES.register("friendly_meteor", () -> EntityType.Builder.<FriendlyMeteor>of(FriendlyMeteor::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new FriendlyMeteor(CtiEntity.FRIENDLY_METEOR.get(),world)).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<AethericMeteor>> AETHERIC_METEOR = ENTITIES.register("aetheric_meteor", () -> EntityType.Builder.<AethericMeteor>of(AethericMeteor::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new AethericMeteor(CtiEntity.AETHERIC_METEOR.get(),world)).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<FieryJavelinProjectile>> FIERY_JAVELIN = ENTITIES.register("fiery_javelin", () -> EntityType.Builder.<FieryJavelinProjectile>of(FieryJavelinProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new FieryJavelinProjectile(CtiEntity.FIERY_JAVELIN.get(),world)).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<PlasmaWaveSlashProjectile>> PLASMA_WAVE_SLASH = ENTITIES.register("plasma_wave_slash", () -> EntityType.Builder.of(PlasmaWaveSlashProjectile::new, MobCategory.MISC).sized(8F, 1F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new PlasmaWaveSlashProjectile(CtiEntity.PLASMA_WAVE_SLASH.get(), world)).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<HomingSunStrike>> HOMING_SUNSTRIKE = ENTITIES.register("homing_sunstrike", () -> EntityType.Builder.of(HomingSunStrike::new, MobCategory.MISC).sized(0.1f, 0.1F).setCustomClientFactory((spawnEntity, world) -> new HomingSunStrike(CtiEntity.HOMING_SUNSTRIKE.get(), world)).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<StarDargonAmmo>> star_dragon_ammo = ENTITY.register(
            "star_dragon_ammo",
            () -> EntityType.Builder.<StarDargonAmmo>of(StarDargonAmmo::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("star_dragon_ammo")
    );

    public static void registerEntityRenderers() {
        ClientHooks.registerEntityRenderer(CtiEntity.TIER_5_ROCKET, RocketRendererTier5::new);
        ClientHooks.registerEntityRenderer(CtiEntity.star_blaze, LargeBrightItemProjectile::new);
        ClientHooks.registerEntityRenderer(CtiEntity.star_ionize, LargeBrightItemProjectile::new);
        ClientHooks.registerEntityRenderer(CtiEntity.star_frozen, LargeBrightItemProjectile::new);
        ClientHooks.registerEntityRenderer(CtiEntity.star_pressure, LargeBrightItemProjectile::new);
        ClientHooks.registerEntityRenderer(CtiEntity.tinker_railgun, TinkerRaligunRenderer::new);
        ClientHooks.registerEntityRenderer(CtiEntity.meteor_entity, MeteorEntityRenderer::new);
        ClientHooks.registerEntityRenderer(CtiEntity.FRIENDLY_METEOR, FriendlyMeteorRenderer::new);
        ClientHooks.registerEntityRenderer(CtiEntity.AETHERIC_METEOR, FriendlyMeteorRenderer::new);
        ClientHooks.registerEntityRenderer(CtiEntity.PLASMA_WAVE_SLASH, renderSlash::new);
        ClientHooks.registerEntityRenderer(CtiEntity.FIERY_JAVELIN, FieryJavelinRender::new);
        ClientHooks.registerEntityRenderer(CtiEntity.HOMING_SUNSTRIKE, RenderSunstrike::new);
    }
    public static void register(IEventBus bus){
        ENTITIES.register(bus);
        ENTITY.register(bus);
    }
}
