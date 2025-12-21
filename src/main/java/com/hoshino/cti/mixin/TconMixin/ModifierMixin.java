package com.hoshino.cti.mixin.TconMixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.Modifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = Modifier.class,remap = false)
public abstract class ModifierMixin {
    @Shadow public abstract int getPriority();

    @Shadow @Nullable protected List<Component> descriptionList;

    @Shadow public abstract String getTranslationKey();

    /**
     * @author EtSH_C2H6S
     * @reason 让词条显示优先级
     */
    @Overwrite
    public List<Component> getDescriptionList(){
        if (descriptionList == null) {
            descriptionList = Arrays.asList(
                    Component.translatable(getTranslationKey() + ".flavor").withStyle(ChatFormatting.ITALIC),
                    Component.translatable(getTranslationKey() + ".description"),
                    Component.translatable("cti.tooltip.modifier.piority").append("§b"+getPriority()));
        }
        return descriptionList;
    }
}
