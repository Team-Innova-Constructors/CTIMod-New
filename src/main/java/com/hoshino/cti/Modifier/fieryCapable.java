package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Modifier.genre.elemental.fiery.BasicBurntModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class fieryCapable extends BasicBurntModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 16*modifierEntry.getLevel();
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (isCorrectSlot && holder != null) {
            holder.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            if (!level.isClientSide&&holder.isInLava() && tool.getDamage() > 0 && EtSHrnd().nextInt(2) == 0) {
                tool.setDamage(tool.getDamage() - 1);
            }
        }
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        var holder = context.getEntity();
        var attacker = source.getEntity();
        if (holder instanceof Player player && attacker!=null&&(attacker.isOnFire()|| EntityTickerManager.getInstancePlayerSpecific(attacker,player.getUUID()).hasTicker(CtiEntityTickers.FIERY.get())))
            amount-=amount*Math.min(0.4f,modifier.getLevel()*0.05f);
        return amount;
    }
}
