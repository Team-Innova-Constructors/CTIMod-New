package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.register.CtiModifiers;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.FakePlayer;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Random;

public class NeutronCollect extends EtSTBaseModifier implements ProcessLootModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.PROCESS_LOOT);
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof FakePlayer) return;
        if (context.getLuck()<0||generatedLoot.isEmpty()) return;
        if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof ServerPlayer &&RANDOM.nextInt(5)<context.getLuck()){
            generatedLoot.remove(RANDOM.nextInt(generatedLoot.size()));
            Item item = tool.getModifierLevel(CtiModifiers.NEUTRON_COLLECT_PLUS.get())>0?ModItems.neutron_nugget.get():ModItems.neutron_pile.get();
            generatedLoot.add(new ItemStack(item,modifier.getLevel()));
        }
    }
}
