package com.hoshino.cti.Modifier;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class EggBoost extends Modifier {
    public EggBoost() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingKilled);
    }

    private void onLivingKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living && event.getEntity() instanceof Chicken chicken) {
            ItemStack stack = living.getItemInHand(living.getUsedItemHand());
            if (stack.getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(stack);
                int lvl = tool.getModifierLevel(this);
                if (lvl > 0) {
                    ItemEntity egg = new ItemEntity(chicken.level, chicken.getX(), chicken.getY(), chicken.getZ(), new ItemStack(Items.EGG, lvl));
                    chicken.level.addFreshEntity(egg);
                }
            }
        }
    }
}
