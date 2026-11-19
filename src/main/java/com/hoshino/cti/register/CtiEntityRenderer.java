package com.hoshino.cti.register;

import com.bobmowzie.mowziesmobs.client.render.entity.RenderSunstrike;
import com.c2h6s.etshtinker.client.render.renderSlash;
import com.hoshino.cti.client.renderer.projectile.*;
import com.hoshino.cti.client.renderer.vehicle.rocketTier5.RocketRendererTier5;
import earth.terrarium.botarium.client.ClientHooks;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = "cti", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CtiEntityRenderer {
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
        ClientHooks.registerEntityRenderer(CtiEntity.THUNDER_BURST, NoopRenderer::new);
        ClientHooks.registerEntityRenderer(CtiEntity.FIERY_SLASH,pContext ->
                new SweepingSlashRenderer(pContext,255,118,27,4));
        ClientHooks.registerEntityRenderer(CtiEntity.RUBY_LASER, RubyLaserRenderer::new);
        ClientHooks.registerEntityRenderer(CtiEntity.MELEE_FIERY_JAVELIN, FieryJavelinRender::new);
    }
}
