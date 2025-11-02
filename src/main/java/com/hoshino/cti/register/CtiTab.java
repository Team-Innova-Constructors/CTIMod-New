package com.hoshino.cti.register;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class CtiTab {
    public static final CreativeModeTab MATERIALS = new CreativeModeTab("cti.materials") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ItemLike) CtiItem.test.get());
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
            return new ItemStack(CtiItem.etbeer.get());
        }
    };

    public CtiTab() {
    }
}
