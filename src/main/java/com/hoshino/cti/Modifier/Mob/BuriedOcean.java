package com.hoshino.cti.Modifier.Mob;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.solidarytinker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BuriedOcean extends BattleModifier {
    private static final ResourceLocation Bonus = solidarytinker.getResource("bonus");

    private static final ThreadLocal<Boolean> IS_PROCESSING = ThreadLocal.withInitial(() -> false);
    public static EntityDamageSource KNLY(LivingEntity entity){
        var s=new EntityDamageSource("keniliyahurt",entity);
        s.bypassArmor().bypassMagic();
        return s;
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (IS_PROCESSING.get()) {
            return;
        }
        var source=event.getSource();
        if (source.getEntity() instanceof Mob mob
                && ModifierUtil.getModifierLevel(mob.getMainHandItem(), this.getId()) > 0
                && !source.getMsgId().equals("keniliyahurt")) {
            try {
                IS_PROCESSING.set(true);
                ModDataNBT nbt = ToolStack.from(mob.getMainHandItem()).getPersistentData();
                int bonus = nbt.getInt(Bonus);
                nbt.putInt(Bonus, Math.min(bonus + 1, 60));
                float damageAmount = mob.getMaxHealth() * 0.03F * bonus;
                event.getEntity().hurt(
                        DamageSource.mobAttack(mob).bypassArmor().bypassMagic(),
                        damageAmount
                );

            } finally {
                IS_PROCESSING.set(false);
            }
        }
    }
}
