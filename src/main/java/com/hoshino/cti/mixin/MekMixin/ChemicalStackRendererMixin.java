package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.chemical.ChemicalStack;
import mekanism.client.jei.ChemicalStackRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ChemicalStackRenderer.class,remap = false)
public class ChemicalStackRendererMixin<STACK extends ChemicalStack<?>> {
    @Inject(method = "getTooltip(Lmekanism/api/chemical/ChemicalStack;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",at =@At("RETURN"), cancellable = true)
    public void addChemicalTooltip(@NotNull STACK stack, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir){
        List<Component> list = cir.getReturnValue();
        list.add(Component.literal("化学品").withStyle(Style.EMPTY.withColor(0x00FFA0)));
        cir.setReturnValue(list);
    }
}
