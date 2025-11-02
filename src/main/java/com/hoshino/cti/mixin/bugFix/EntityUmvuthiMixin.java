package com.hoshino.cti.mixin.bugFix;

import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoEntityRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Mixin(RenderUmvuthi.class)
public abstract class EntityUmvuthiMixin extends MowzieGeoEntityRenderer<EntityUmvuthi> {

    protected EntityUmvuthiMixin(EntityRendererProvider.Context renderManager, AnimatedGeoModel<EntityUmvuthi> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Redirect(
            remap = false,
            method = "render(Lcom/bobmowzie/mowziesmobs/server/entity/umvuthana/EntityUmvuthi;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lcom/bobmowzie/mowziesmobs/server/entity/umvuthana/EntityUmvuthi;updateRattleSound(F)V")
    )
    public void updateRattleSound(EntityUmvuthi instance, float maskRot) {
        if (instance !=null){
            ((EntityUmvuthi)this.animatable).updateRattleSound(maskRot);

        }
    }
}
