package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.register.CtiItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class MeteorEntityRenderer extends EntityRenderer<Projectile> {
    public ItemRenderer itemRenderer;
    public final float size;

    public MeteorEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.itemRenderer = p_174008_.getItemRenderer();
        this.size = 1;
    }
    public MeteorEntityRenderer(EntityRendererProvider.Context p_174008_,float size) {
        super(p_174008_);
        this.itemRenderer = p_174008_.getItemRenderer();
        this.size = size;
    }

    @Override
    public void render(Projectile entity, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        p_114488_.pushPose();
        p_114488_.mulPose(Vector3f.YP.rotationDegrees(entity.tickCount % 360));
        p_114488_.mulPose(Vector3f.XP.rotationDegrees(entity.tickCount % 360));
        p_114488_.mulPose(Vector3f.ZP.rotationDegrees(entity.tickCount % 360));
        p_114488_.translate(-0.03125, -0.09375, 0);
        p_114488_.scale(size,size,size);
        this.itemRenderer.renderStatic(new ItemStack(CtiItem.meteorite_ore.get()), ItemTransforms.TransformType.GROUND, p_114490_, OverlayTexture.NO_OVERLAY, p_114488_, p_114489_, entity.getId());
        p_114488_.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile meteorEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
