package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.event.hooks.AbilityHooks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.hoshino.cti.Modifier.aetherCompact.Atheric.TAG_AETHER;

@Mixin(value = AbilityHooks.ToolHooks.class,remap = false)
public class AbilityHooks$ToolHooksMixin {
    @Inject(method = "reduceToolEffectiveness",at = @At("HEAD"),cancellable = true)
    private static void avoidNerfForTool(Player player, BlockState state, ItemStack stack, float speed, CallbackInfoReturnable<Float> cir){
        if (stack.getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(stack);
            if (toolStack.getModifierLevel(new ModifierId("cti:zanite"))>0){
                cir.setReturnValue(speed);
                return;
            }
            for (MaterialVariant material:toolStack.getMaterials().getList()){
                if (MaterialRegistry.getInstance().isInTag(material.getId(),TAG_AETHER)){
                    cir.setReturnValue(speed);
                    return;
                }
            }
        }
    }
}
