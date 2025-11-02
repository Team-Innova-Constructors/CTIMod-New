package com.hoshino.cti.Modifier.Replace;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public class FixedArmorOracular extends Modifier implements InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity holder, int slot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isCorrectSlot) {
            Collection<MobEffectInstance> harmeffect = holder.getActiveEffects();
            for (int i = 0; i < harmeffect.size(); i++) {
                MobEffectInstance effect = harmeffect.stream().toList().get(i);
                MobEffect harm = effect.getEffect();
                if (harm.getCategory() == MobEffectCategory.HARMFUL) {
                    holder.removeEffect(harm);
                }
            }
        }
    }
}
