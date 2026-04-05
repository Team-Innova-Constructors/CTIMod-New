package com.hoshino.cti.Modifier.Replace;

import com.xiaoyue.tinkers_ingenuity.generic.XICModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplaceRingNecrotic extends XICModifier {
    public void onCurioToDamage(IToolStackView curio, LivingDamageEvent event, LivingEntity attacker, LivingEntity target, int level) {
        attacker.heal(Math.min( event.getAmount() * 0.05F * level,attacker.getMaxHealth()*0.05f*level));
    }
}

