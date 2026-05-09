package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.hoshino.cti.content.materialGenre.GenreManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.ArmorLootingModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.HarvestEnchantmentsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.LootingContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.List;
import java.util.Map;

public class OverProperty extends Modifier implements HarvestEnchantmentsModifierHook, ProcessLootModifierHook, ArmorLootingModifierHook, EnchantmentModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.HARVEST_ENCHANTMENTS,ModifierHooks.PROCESS_LOOT,ModifierHooks.ARMOR_LOOTING,ModifierHooks.ENCHANTMENTS);
    }

    @Override
    public int updateEnchantmentLevel(IToolStackView tool, ModifierEntry modifier, Enchantment enchantment, int level) {
        if ((enchantment==Enchantments.BLOCK_FORTUNE||enchantment==Enchantments.MOB_LOOTING)&&enoughOverslime(tool))
            level+= modifier.getLevel();
        return level;
    }

    @Override
    public void updateEnchantments(IToolStackView tool, ModifierEntry modifier, Map<Enchantment, Integer> map) {
        int fortune = map.getOrDefault(Enchantments.BLOCK_FORTUNE,0);
        int looting = map.getOrDefault(Enchantments.MOB_LOOTING,0);
        if (enoughOverslime(tool)){
            map.put(Enchantments.MOB_LOOTING,looting+modifier.getLevel());
            map.put(Enchantments.BLOCK_FORTUNE,fortune+modifier.getLevel());
        }
    }

    public static boolean enoughOverslime(IToolStackView tool){
        var os = TinkerModifiers.overslime.get();
        return os.getShield(tool)>=tool.getStats().getInt(GenreManager.OVERSLIME_GENRE.consumption);
    }
    public static void consumeOverslime(IToolStackView tool,ModifierEntry entry){
        var os = TinkerModifiers.overslime.get();
        os.addOverslime(tool,entry,-tool.getStats().getInt(GenreManager.OVERSLIME_GENRE.consumption));
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        if (enoughOverslime(tool)) consumeOverslime(tool,modifier);
    }

    @Override
    public int updateArmorLooting(IToolStackView tool, ModifierEntry modifier, LootingContext context, EquipmentContext equipment, EquipmentSlot slot, int looting) {
        if (enoughOverslime(tool)){
            looting+=modifier.getLevel();
            if (!context.getHolder().level.isClientSide)consumeOverslime(tool,modifier);
        }
        return looting;
    }

    @Override
    public void updateHarvestEnchantments(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context, EquipmentContext equipment, EquipmentSlot slot, Map<Enchantment, Integer> map) {
        int fortune = map.getOrDefault(Enchantments.BLOCK_FORTUNE,0);
        if (enoughOverslime(tool)){
            map.put(Enchantments.BLOCK_FORTUNE,fortune+modifier.getLevel());
            if (!context.getWorld().isClientSide)consumeOverslime(tool,modifier);
        }
    }
}
