package com.hoshino.cti.Modifier.Contributors;

import appeng.core.definitions.AEItems;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class AlienMatter extends BattleModifier {
    @Override
    public void processLoot(IToolStackView iToolStackView, ModifierEntry modifierEntry, List<ItemStack> list, LootContext lootContext) {
        list.add(new ItemStack(AEItems.CERTUS_QUARTZ_CRYSTAL));
        list.add(new ItemStack(AEItems.FLUIX_CRYSTAL));
    }
}
