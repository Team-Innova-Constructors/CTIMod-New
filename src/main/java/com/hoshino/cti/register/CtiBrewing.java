package com.hoshino.cti.register;

import com.marth7th.solidarytinker.register.solidarytinkerItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class CtiBrewing {
    public static void init() {
        PotionBrewing.addMix(Potions.AWKWARD, solidarytinkerItem.violane.get(), CtiPotions.BLOODANGER.get());
        PotionBrewing.addMix(CtiPotions.BLOODANGER.get(), Items.REDSTONE, CtiPotions.LONG_BLOODANGER.get());
        PotionBrewing.addMix(CtiPotions.BLOODANGER.get(), Items.GLOWSTONE_DUST, CtiPotions.STRONG_BLOODANGER.get());

        PotionBrewing.addMix(Potions.AWKWARD, Items.OBSIDIAN, CtiPotions.resolute.get());
        PotionBrewing.addMix(CtiPotions.resolute.get(), Items.REDSTONE, CtiPotions.long_resolute.get());
    }
}
