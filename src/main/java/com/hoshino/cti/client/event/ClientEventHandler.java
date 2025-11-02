package com.hoshino.cti.client.event;

import com.hoshino.cti.Screen.AtmosphereCondensatorScreen;
import com.hoshino.cti.Screen.AtmosphereExtractorScreen;
import com.hoshino.cti.Screen.ReactorNeutronCollectorScreen;
import com.hoshino.cti.Screen.menu.ctiMenu;
import com.hoshino.cti.client.CtiKeyBinding;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.client.InitPartModel;
import com.hoshino.cti.Cti;
import com.hoshino.cti.client.hud.CurseInfoHud;
import com.hoshino.cti.client.hud.EnvironmentalHud;
import com.hoshino.cti.client.particle.*;
import com.hoshino.cti.client.particle.ParticleType.StarFallParticleProvider;
import com.hoshino.cti.client.renderer.projectile.StarDragonAmmoRenderer;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.StarHitPacket;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.util.Vec3Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;


public class ClientEventHandler {

    @Mod.EventBusSubscriber(modid = Cti.MOD_ID,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Mods {

        @SubscribeEvent
        public static void registerLoader(ModelEvent.RegisterGeometryLoaders event){
            InitPartModel.init(event::register);
        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.register(CtiParticleType.STAR_LINE.get(), StarLineParticle::provider);
            event.register(CtiParticleType.RED_SPARK.get(), RedSparkParticle::provider);
            event.register(CtiParticleType.FIERY_EXPLODE.get(), FieryExplodeParticle::provider);
            event.register(CtiParticleType.FIERY_LINE.get(), FieryJavelinLineParticle::provider);
            event.register(CtiParticleType.ION.get(), IonParticle::provider);
            event.register(CtiParticleType.STARFALL.get(), StarFallParticleProvider::new);
        }
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ctiMenu.ATMOSPHERE_EXT_MENU.get(), AtmosphereExtractorScreen::new);
            MenuScreens.register(ctiMenu.ATMOSPHERE_CON_MENU.get(), AtmosphereCondensatorScreen::new);
            MenuScreens.register(ctiMenu.NEUT_COL_MENU.get(), ReactorNeutronCollectorScreen::new);
            event.enqueueWork(CtiEntity::registerEntityRenderers);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(CtiEntity.star_dragon_ammo.get(), StarDragonAmmoRenderer::new);
        }

        @SubscribeEvent
        public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                event.registerAboveAll("ionize", EnvironmentalHud.ENVIRONMENT_OVERLAY);
                event.registerAboveAll("curse", CurseInfoHud.CurseHUD);
            }
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(CtiKeyBinding.STAR_HIT);
        }

        //    @SubscribeEvent
//    public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, ? extends EntityModel<LivingEntity>> event) {
//        LivingEntity entity = event.getEntity();
//        PoseStack poseStack = event.getPoseStack();
//        MultiBufferSource bufferSource = event.getMultiBufferSource();
//        if (Minecraft.getInstance().player != null && entity instanceof Mob mob) {
//            poseStack.pushPose();
//            float heightOffset = entity.getBbHeight() + 3F;
//            poseStack.translate(0F, heightOffset, 0F);
//            poseStack.mulPose(Vector3f.YP.rotationDegrees(-entity.getYRot()));
//            poseStack.mulPose(Vector3f.XP.rotationDegrees(90F));
//            poseStack.mulPose(Vector3f.YP.rotationDegrees(225F));
//            poseStack.mulPose(Vector3f.XP.rotationDegrees(-90F));
//            poseStack.scale(2.5F, 2.5F, 2.5F);
//            ItemStack ironSword = new ItemStack(Items.IRON_SWORD);
//            Minecraft.getInstance().getItemRenderer().renderStatic(ironSword, ItemTransforms.TransformType.NONE, event.getPackedLight(), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.getId());
//            poseStack.popPose();
//        }
//    }


    }

    @Mod.EventBusSubscriber(modid = Cti.MOD_ID,value = Dist.CLIENT,bus =Mod.EventBusSubscriber.Bus.FORGE)
    public static class Forge {
        @SubscribeEvent
        public static void onKeyPressed(InputEvent.Key event) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (player.level.isClientSide()) {
                    if (CtiKeyBinding.STAR_HIT.consumeClick()) {
                        var mob= Vec3Helper.getPointedEntity(player,player.getLevel(),50, Mob.class, Mob::isAlive, Mob -> false);
                        if (mob != null) {
                            CtiPacketHandler.sendToServer(new StarHitPacket(mob.getUUID()));
                        }
                    }
                }
            }
        }
    }
}
