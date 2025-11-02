package com.hoshino.cti.Modifier.Armor;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Breeding extends EtSTBaseModifier {
    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (holder != null && isCorrectSlot) {
            List<Animal> list = holder.level.getEntitiesOfClass(Animal.class, new AABB(holder.blockPosition()).inflate(modifier.getLevel() * 5));
            for (Animal animal : list) {
                if (animal != null) {
                    if (animal.getAge() < 0) {
                        animal.setAge(animal.getAge() + 2 * modifier.getLevel());
                    } else {
                        animal.setAge(0);
                    }
                }
            }
        }
    }
}
