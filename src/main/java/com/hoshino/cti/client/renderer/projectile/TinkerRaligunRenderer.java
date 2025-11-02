package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.Entity.Projectiles.TinkerRailgunProjectile;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PRailgunC2S;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

public class TinkerRaligunRenderer extends EntityRenderer<TinkerRailgunProjectile> {
    public ItemRenderer itemRenderer;

    public TinkerRaligunRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.itemRenderer = p_174008_.getItemRenderer();
    }

    @Override
    public void render(TinkerRailgunProjectile p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        if (p_114485_.stack != null && p_114485_.stack.getItem() instanceof ToolPartItem item) {
            if (MaterialVariant.of(item.getMaterial(p_114485_.stack)).isEmpty() || MaterialVariant.of(item.getMaterial(p_114485_.stack)).isUnknown()) {
                CtiPacketHandler.sendToServer(new PRailgunC2S(p_114485_.getId()));
            } else {
                p_114488_.pushPose();
                p_114488_.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_114487_, p_114485_.yRotO, p_114485_.getYRot()) - 90f));
                p_114488_.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_114487_, p_114485_.xRotO, p_114485_.getXRot()) - 63.52f));
                p_114488_.translate(-0.03125, -0.09375, 0);
                this.itemRenderer.renderStatic(p_114485_.stack, ItemTransforms.TransformType.GROUND, p_114490_, OverlayTexture.NO_OVERLAY, p_114488_, p_114489_, p_114485_.getId());
                p_114488_.popPose();
            }
        }
    }

    @Override
    protected int getSkyLightLevel(TinkerRailgunProjectile p_114509_, BlockPos p_114510_) {
        return 15;
    }

    @Override
    protected int getBlockLightLevel(TinkerRailgunProjectile p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(TinkerRailgunProjectile arrow) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
