package com.hoshino.cti.Modifier;

import com.hoshino.cti.recipe.RecipeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.FakePlayer;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Optional;

public class PlateSmashing extends NoLevelsModifier implements ProcessLootModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        var playerParam = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (playerParam instanceof Player&&!(playerParam instanceof FakePlayer)){
            List.copyOf(generatedLoot).forEach(stack -> {
                var item = stack.getItem();
                Optional.ofNullable(RecipeMap.getPlateSmashRecipes().get(item)).ifPresent(plateSmashRecipe -> {
                    generatedLoot.remove(stack);
                    generatedLoot.add(plateSmashRecipe.getResultItem());
                });
            });
        }
    }
}
