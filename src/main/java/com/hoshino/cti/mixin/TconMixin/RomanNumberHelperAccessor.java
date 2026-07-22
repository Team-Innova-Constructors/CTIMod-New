package com.hoshino.cti.mixin.TconMixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import slimeknights.tconstruct.library.utils.RomanNumeralHelper;

import java.util.List;

@Mixin(value = RomanNumeralHelper.class, remap = false)
public interface RomanNumberHelperAccessor {
    @Accessor("TRANSLATION_KEY_PREFIX")
    static String getTranslationKeyPrefix() {
        throw new AssertionError();
    }

    @Accessor("NUMERAL_CACHE")
    static Int2ObjectMap<Component> getNumeralCache() {
        throw new AssertionError();
    }

    @Invoker("intToRomanNumeral")
    static String invokeIntToRomanNumeral(int value) {
        throw new AssertionError();
    }
}
