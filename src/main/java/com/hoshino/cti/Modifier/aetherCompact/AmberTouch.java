package com.hoshino.cti.Modifier.aetherCompact;

import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.item.AetherItems;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.mixin.CuriosMixinHooks;

import java.util.List;

public class AmberTouch extends NoLevelsModifier implements ProcessLootModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.BLOCK_STATE)){
            var block = context.getParam(LootContextParams.BLOCK_STATE).getBlock();
            if (block== AetherBlocks.GOLDEN_OAK_LOG.get()){
                if (((ToolStack)tool).createStack().getEnchantmentLevel(Enchantments.SILK_TOUCH)<1){
                    var fortune = ((ToolStack)tool).createStack().getEnchantmentLevel(Enchantments.BLOCK_FORTUNE)+ CuriosMixinHooks.getFortuneLevel(context);
                    generatedLoot.add(new ItemStack(AetherItems.GOLDEN_AMBER.get(),
                            CommonUtil.applyOreFortuneBonus(context.getRandom(),2,fortune)));
                }
            }
        }
    }
}
