package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class BuffedAnger extends EtSTBaseModifier {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity living = context.getAttacker();
        float percentage = (living.getMaxHealth()-living.getHealth())/living.getMaxHealth();
        if (percentage>0.7) percentage*=2;
        if (living.getMaxHealth()>100) percentage/=2;
        return damage*(1+percentage);
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity living, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean primary) {
        if (abstractArrow != null){
            float percentage = (living.getMaxHealth()-living.getHealth())/living.getMaxHealth();
            if (percentage>0.67) percentage*=2;
            if (living.getMaxHealth()>100) percentage/=2;
            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage()*(1+percentage));
        }
    }
}
