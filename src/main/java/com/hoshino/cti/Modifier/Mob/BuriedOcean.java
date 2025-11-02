package com.hoshino.cti.Modifier.Mob;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.solidarytinker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BuriedOcean extends BattleModifier {
    private static final ResourceLocation Bonus = solidarytinker.getResource("bonus");

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Mob mob && ModifierUtil.getModifierLevel(mob.getMainHandItem(), this.getId()) > 0 && !event.getSource().isMagic()) {
            ModDataNBT nbt = ToolStack.from(mob.getMainHandItem()).getPersistentData();
            int bonus = nbt.getInt(Bonus);
            nbt.putInt(Bonus, Math.min(bonus + 1, 60));
            event.getEntity().hurt(DamageSource.mobAttack(mob).setMagic().bypassArmor().bypassMagic(), mob.getMaxHealth() * 0.03F * bonus);
        }
    }
}
