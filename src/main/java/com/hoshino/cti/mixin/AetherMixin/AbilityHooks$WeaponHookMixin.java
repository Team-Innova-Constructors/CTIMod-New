package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.event.hooks.AbilityHooks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.hoshino.cti.Modifier.aetherCompact.Atheric.TAG_AETHER;

@Mixin(value = AbilityHooks.WeaponHooks.class,remap = false)
public class AbilityHooks$WeaponHookMixin {
    @Inject(method = "reduceWeaponEffectiveness",at = @At("HEAD"),cancellable = true)
    private static void avoidNerfForWeapon(LivingEntity target, Entity source, float damage, CallbackInfoReturnable<Float> cir) {
        if (source instanceof LivingEntity living) {
            ItemStack stack = living.getMainHandItem();
            if (stack.getItem() instanceof IModifiable) {
                ToolStack toolStack = ToolStack.from(stack);
                if (toolStack.getModifierLevel(new ModifierId("cti:zanite")) > 0) {
                    cir.setReturnValue(damage);
                    return;
                }
                for (MaterialVariant material : toolStack.getMaterials().getList()) {
                    if (MaterialRegistry.getInstance().isInTag(material.getId(), TAG_AETHER)) {
                        cir.setReturnValue(damage);
                        return;
                    }
                }
            }
        } else if (source instanceof Projectile) cir.setReturnValue(damage);
    }
}
