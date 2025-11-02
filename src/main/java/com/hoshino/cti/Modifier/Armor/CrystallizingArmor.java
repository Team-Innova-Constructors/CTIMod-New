package com.hoshino.cti.Modifier.Armor;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CrystallizingArmor extends EtSTBaseModifier {
    public static final ResourceLocation KEY_REDUCE = Cti.getResource("crystallizing");
    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (level.getGameTime()%20==0){
            int existing = tool.getPersistentData().getInt(KEY_REDUCE);
            if (existing<20) existing++;
            tool.getPersistentData().putInt(KEY_REDUCE,existing);
        }
    }

    @Override
    public float modifierDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        int charge =tool.getPersistentData().getInt(KEY_REDUCE);
        if (charge>0){
            boolean b = amount>1;
            amount*=1-0.05f*charge;
            if (b) {
                tool.getPersistentData().putInt(KEY_REDUCE,charge-1);
            }
        }
        return amount;
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return super.getDisplayName().copy().append(" "+tool.getPersistentData().getInt(KEY_REDUCE)*5+"%");
    }
}
