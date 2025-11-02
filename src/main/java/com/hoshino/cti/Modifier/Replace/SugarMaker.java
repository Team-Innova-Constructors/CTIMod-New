package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.register.CtiItem;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class SugarMaker extends BattleModifier {
    @Override
    public void processLoot(IToolStackView iToolStackView, ModifierEntry modifierEntry, List<ItemStack> list, LootContext lootContext) {
        if (!(lootContext.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof Player player)) return;
        boolean should=player.level.random.nextInt(10)>2;
        if(!should)return;
        final int[] rule = {94, 69, 34, 14, 0};
        final Item[] items = {
                CtiItem.nakshatra_sugar.get(),
                CtiItem.ha_sugar.get(),
                CtiItem.strong_sugar.get(),
                CtiItem.heng_sugar.get(),
                CtiItem.covert_sugar.get()
        };
        int randomValue = player.level.random.nextInt(100);
        for (int i = 0; i < rule.length; i++) {
            if (randomValue > rule[i]) {
                list.add(new ItemStack(items[i]));
                return;
            }
        }
    }
}
