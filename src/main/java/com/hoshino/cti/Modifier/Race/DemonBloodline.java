package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;


public class DemonBloodline extends NoLevelsModifier {

    public static boolean hasBloodline(LivingEntity entity) {
        if (entity == null) return false;
        return GetModifierLevel.getTotalArmorModifierlevel(entity, CtiModifiers.demon_bloodline.getId())>0;
    }
}
