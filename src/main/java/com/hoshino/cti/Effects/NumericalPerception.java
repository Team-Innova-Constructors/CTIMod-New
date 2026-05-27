package com.hoshino.cti.Effects;

import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.hoshino.cti.register.CtiModifiers.kingdomofnumbers;

public class NumericalPerception extends StaticMobEffect {
    public NumericalPerception() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingHurt);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "B764A8F6-88CE-85C1-C5F9-C832E5335E2D", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
        super.addAttributeModifier(ForgeMod.ATTACK_RANGE.get(), "0F40ADF0-3D9A-D8B7-79EE-27AB356A0050", 0.5, AttributeModifier.Operation.ADDITION);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getSource().getEntity();
        if (a instanceof Player player && event.getEntity() != null&&player.hasEffect(CtiEffects.numerical_perception.get())) {
            event.setAmount(event.getAmount()*2f);
        }
    }
}
