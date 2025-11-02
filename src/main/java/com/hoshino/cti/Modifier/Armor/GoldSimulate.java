package com.hoshino.cti.Modifier.Armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class GoldSimulate extends NoLevelsModifier {
    public GoldSimulate() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::OnChangeTarget);
    }

    private void OnChangeTarget(LivingChangeTargetEvent event) {
        boolean b = false;
        LivingEntity living = event.getNewTarget();
        if (living != null) {
            for (EquipmentSlot slot : List.of(EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS)) {
                ItemStack stack = living.getItemBySlot(slot);
                if (stack.getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this) > 0) {
                        b = true;
                        break;
                    }
                }
            }
        }
        if (b && event.getEntity() instanceof ZombifiedPiglin piglin && piglin.getLastHurtByMob() != event.getNewTarget()) {
            event.setCanceled(true);
        }
    }
}
