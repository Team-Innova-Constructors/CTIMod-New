package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.utils.ToolUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Iterator;
import java.util.Objects;

@Mixin(value = ToolUtils.class, remap = false)
public class ToolUtilsMixin {
    @Inject(at = {@At("RETURN")}, method = {"hasMetaIn"}, cancellable = true)
    private static void addSplendid(ToolStack tool, String id, CallbackInfoReturnable<Boolean> cir) {
        Iterator<MaterialVariant> var2 = tool.getMaterials().getList().iterator();
        if (var2.hasNext()) {
            MaterialVariant material = var2.next();
            cir.setReturnValue(material.get().getIdentifier().toString().equals(id));
        }
        if (!cir.getReturnValueZ() && Objects.equals(id, "tinkers_ingenuity:splendid") && tool.getModifierLevel(new ModifierId("tinkers_ingenuity:glorious")) > 0) {
            cir.setReturnValue(true);
        }
    }
}
