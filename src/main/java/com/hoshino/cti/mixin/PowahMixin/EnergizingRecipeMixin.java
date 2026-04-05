package com.hoshino.cti.mixin.PowahMixin;

import cofh.thermal.lib.common.ThermalIDs;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.lib.logistics.inventory.RecipeWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(value = EnergizingRecipe.class,remap = false)
public abstract class EnergizingRecipeMixin implements Recipe<RecipeWrapper> {
    /**
     * @author EtSH_C2H6S
     * @reason 让充能台配方跳过槽楔
     */
    @Overwrite
    public boolean matches(RecipeWrapper inv, Level world) {
        List<Ingredient> stacks = new ArrayList<>(getIngredients());
        for (int i = 1; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            var seal = ForgeRegistries.ITEMS.getValue(CommonUtil.SLOT_SEAL);
            if (!stack.isEmpty()&&stack.getItem()!=seal) {
                boolean flag = false;
                Iterator<Ingredient> itr = stacks.iterator();
                while (itr.hasNext()) {
                    Ingredient ingredient = itr.next();
                    if (ingredient.test(stack)) {
                        flag = true;
                        itr.remove();
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
        }
        return stacks.isEmpty();
    }
}
