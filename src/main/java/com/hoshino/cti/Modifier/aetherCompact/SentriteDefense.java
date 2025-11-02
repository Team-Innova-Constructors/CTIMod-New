package com.hoshino.cti.Modifier.aetherCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SentriteDefense extends EtSTBaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float modifierDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (!source.isBypassArmor()&&!source.isBypassInvul()){
            return amount*0.5f;
        }
        return amount;
    }
}
