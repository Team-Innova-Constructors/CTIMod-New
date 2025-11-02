package com.hoshino.cti.Modifier.Replace;

import com.marth7th.solidarytinker.util.method.ModifierLevel;
import com.xiaoyue.tinkers_ingenuity.content.library.context.ArmorAttackContext;
import com.xiaoyue.tinkers_ingenuity.generic.XIModifier;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CrippleSupperMass extends XIModifier {
    @Override
    public void onTakeHurt(IToolStackView armor, LivingHurtEvent event, ArmorAttackContext context, int level) {
        int TotalLevel = ModifierLevel.getTotalArmorModifierlevel(context.entity(), this.getId());
        float cost = context.entity().getArmorValue() * 0.001f * TotalLevel;
        if (event.getSource().isBypassArmor()) {
            event.setAmount(event.getAmount() * (1 - Math.min(0.55f, cost)));
        } else event.setAmount(event.getAmount() * (1 - Math.min(0.1F, cost)));
    }
}
