package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.util.slotUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class TrinityCurse extends EtSTBaseModifier {
    public TrinityCurse() {
        MinecraftForge.EVENT_BUS.addListener(this::LivingHurt);
    }

    private void LivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity != null) {
            for (EquipmentSlot slot : slotUtil.ALL) {
                ItemStack stack = entity.getItemBySlot(slot);
                if (stack.getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this) > 0 && tool.getModifierLevel(this) != 3) {
                        event.setAmount(event.getAmount() * 1.33f);
                        return;
                    } else if (tool.getModifierLevel(this) == 3) {
                        event.setAmount(event.getAmount() * 3.33f);
                        return;
                    }
                }
            }
        }
    }

    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (modifier.getLevel() > 0 && isCorrectSlot && holder != null) {
            if (holder.invulnerableTime > 0) {
                holder.invulnerableTime -= 1;
                if (modifier.getLevel() == 3 && holder.invulnerableTime > 0) {
                    holder.invulnerableTime = 0;
                }
            }
        }
    }


}
