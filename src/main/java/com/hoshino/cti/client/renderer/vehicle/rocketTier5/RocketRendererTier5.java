package com.hoshino.cti.client.renderer.vehicle.rocketTier5;

import com.hoshino.cti.Entity.vehicles.rocketTier5;
import earth.terrarium.ad_astra.client.renderer.entity.vehicle.VehicleRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RocketRendererTier5 extends VehicleRenderer<rocketTier5, RocketModelTier5> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("cti", "textures/vehicles/tier_5_rocket.png");

    public RocketRendererTier5(EntityRendererProvider.Context context) {
        super(context, new RocketModelTier5(context.bakeLayer(RocketModelTier5.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public RocketModelTier5 getModel() {
        return null;
    }

    public @NotNull ResourceLocation getTextureLocation(rocketTier5 entity) {
        return TEXTURE;
    }
}
