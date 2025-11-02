package com.hoshino.cti.mixin.AdAstraMixin;

import com.hoshino.cti.Capabilitiess.IFreezeShielding;
import com.hoshino.cti.Modifier.Base.OxygenConsumeModifier;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.BiomeUtil;
import com.hoshino.cti.util.CtiTagkey;
import earth.terrarium.ad_astra.common.util.ModUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

@Mixin(value = ModUtils.class, remap = false)
public abstract class ModUtilMixin {
    @Inject(method = "getEntityGravity", at = @At("TAIL"), cancellable = true)
    private static void getEntityGravity(Entity entity, CallbackInfoReturnable<Float> callbackInfo) {
        float gravity = callbackInfo.getReturnValueF();
        if (gravity != 1.0F) {
            if (entity instanceof LivingEntity living) {

                for (ItemStack stack : List.of(living.getItemBySlot(EquipmentSlot.HEAD), living.getItemBySlot(EquipmentSlot.CHEST), living.getItemBySlot(EquipmentSlot.FEET), living.getItemBySlot(EquipmentSlot.LEGS))) {
                    if (stack.getItem() instanceof IModifiable && ToolStack.from(stack).getModifierLevel(CtiModifiers.gravity_normalizer.get()) > 0) {
                        callbackInfo.setReturnValue(1f);
                    } else if (stack.getTags().toList().contains(CtiTagkey.ENVIRONMENT_ADV)) {
                        callbackInfo.setReturnValue(1f);
                    }
                }
            }
        }
        if (entity instanceof LivingEntity living) {
            if (BiomeUtil.getBiomeKey(living.level.getBiome(living.blockPosition())) == BiomeUtil.DISORDERED_ZONE) {
                if (living.level.getGameTime() % 5 == 0) {
                    living.getPersistentData().putFloat("cti.rnd_gravity", EtSHrnd().nextFloat() * 4 - 1);
                }
                callbackInfo.setReturnValue(living.getPersistentData().getFloat("cti.rnd_gravity"));
            }
        }

    }

    @Inject(method = "armourIsOxygenated", at = @At("TAIL"), cancellable = true)
    private static void armourIsOxygenated(LivingEntity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
        boolean oxygenated = callbackInfo.getReturnValueZ();
        if (!oxygenated) {
            ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
            if (stack.getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(stack);
                for (ModifierEntry entry:tool.getModifierList()){
                    if (entry.getModifier() instanceof OxygenConsumeModifier modifier&&modifier.hasOxygen(tool,entry)) {
                        if (!entity.level.isClientSide) modifier.consumeOxygen(tool,entry);
                        callbackInfo.setReturnValue(true);
                        return;
                    }
                }
            } else if (stack.getTags().toList().contains(CtiTagkey.OXYGEN_REGEN)) {
                callbackInfo.setReturnValue(true);
            }
        }

    }
}
