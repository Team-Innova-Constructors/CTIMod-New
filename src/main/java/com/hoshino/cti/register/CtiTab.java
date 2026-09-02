package com.hoshino.cti.register;

import cofh.thermal.foundation.init.TFndItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CtiTab {
    public static final List<String> EXTRA_MATERIAL_ITEMS = List.of(
            "thermal:ruby_ore",
            "thermal:deepslate_ruby_ore",
            "thermal:ruby",
            "thermal:ruby_block"
    );
    public static final CreativeModeTab MATERIALS = new CreativeModeTab("cti.materials") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ItemLike) CtiItem.test.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            super.fillItemList(pItems);
            ForgeRegistries.ITEMS.getKeys().stream().filter(resourceLocation ->
                    EXTRA_MATERIAL_ITEMS.contains(resourceLocation.toString())).forEach(resourceLocation ->
                    pItems.add(new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation))));
        }
    };
    public static final CreativeModeTab MIXC = new CreativeModeTab("cti.mixc") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ItemLike) CtiItem.unipolar_magnet.get());
        }
    };
    public static final CreativeModeTab MACHINE = new CreativeModeTab("cti.machine") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CtiItem.atmosphere_condensator.get());
        }
    };
    public static final CreativeModeTab FOOD = new CreativeModeTab("cti.food") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CtiItem.covert_sugar.get());
        }
    };

    public CtiTab() {
    }
}
