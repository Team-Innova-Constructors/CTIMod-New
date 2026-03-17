package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.register.CtiItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class ReplacedOvergrowth extends EtSTBaseModifier implements IModifierWithSpecialDesc {
    @Override
    public String getDesc() {
        return "info.cti.overslime";
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide && holder.tickCount % Math.max(10,40-(modifier.getLevel()*10)) == 0) {
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
            if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                overslime.addOverslime(tool, entry, 1);
            }
        }
    }
}
