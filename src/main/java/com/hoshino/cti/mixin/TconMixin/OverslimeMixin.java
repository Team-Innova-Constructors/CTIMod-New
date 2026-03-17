package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.Modifier.aetherCompact.AmbrosiumPowered;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.DurabilityShieldModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

@Mixin(value = OverslimeModifier.class,remap = false)
public abstract class OverslimeMixin extends DurabilityShieldModifier {
    @Shadow public abstract int getShieldCapacity(IToolStackView tool, ModifierEntry modifier);

    @ModifyConstant(method = "<clinit>",constant = @Constant(floatValue = Short.MAX_VALUE))
    @Unique
    private static float modifyOverslimeMaxValue(float constant) {
        return Integer.MAX_VALUE;
    }

    @Inject(at = @At(value = "TAIL"),method = "addOverslime")
    private void onOverslimeChange(IToolStackView tool, ModifierEntry entry, int amount, CallbackInfo ci){
        var current = getShield(tool);
        var max = getShieldCapacity(tool,entry);
        var actualAmount = Mth.clamp(amount,-current,max);
        if (actualAmount<0&&tool.getModifierLevel(CtiModifiers.OVER_AETHERIC.getId())>0)
            AmbrosiumPowered.chargeTool(tool,-actualAmount*4);
    }
}
