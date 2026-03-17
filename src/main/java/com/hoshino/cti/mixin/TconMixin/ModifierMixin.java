package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
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
            List<Component> list = new ArrayList<>(List.of(
                    Component.translatable(getTranslationKey() + ".flavor").withStyle(ChatFormatting.ITALIC),
                    Component.translatable(getTranslationKey() + ".description")));
            if ((Modifier)(Object) this instanceof IModifierWithSpecialDesc specialDesc)
                list.add(Component.translatable(specialDesc.getDesc()));
            if (!String.valueOf(getPriority()).isEmpty())
                list.add(Component.translatable("cti.tooltip.modifier.piority").append("§b" + getPriority()));
            descriptionList = list;
        }
        return descriptionList;
    }
}
