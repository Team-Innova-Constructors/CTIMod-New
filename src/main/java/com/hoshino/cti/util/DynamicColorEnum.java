package com.hoshino.cti.util;

import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import slimeknights.tconstruct.library.utils.RomanNumeralHelper;

import javax.annotation.Nullable;

public enum DynamicColorEnum {
    HOSHINO_SCROLL(Type.SCROLL, new int[]{0xffea95, 0xffaaff, 0x55c4ff}, 20, 20),
    MAGIC_ICE(Type.BREATH, new int[]{0x99b1ff}, 60, 1000, 0.3f),
    HOSHINO_REMIX(Type.REMIX, new int[]{0xffea95, 0xffaaff, 0x55c4ff}, 30, 20, 60, 1000, 0.35f),
    INDUSTRIAL(Type.BREATH, new int[]{0x89919C}, 60, 1000, 0.5f),
    REFINED(Type.SCROLL, new int[]{0xA48DFF, 0xa9bcff, 0xb9e5ff, 0x77aeff}, 60, 20),
    THE_RELIC(Type.REMIX, new int[]{0x5d00ff, 0x5825ff, 0x7e3dff}, 30, 15, 60, 800, 0.35f),
    SHADOW_OF_VIGRID(Type.REMIX, new int[]{0xffff43, 0xc9ffa5, 0xfdffde}, 30, 15, 60, 800, 0.2f);

    public enum Type {
        SCROLL, BREATH, REMIX
    }

    @Getter
    private final Type type;
    @Getter
    private final int[] colorArray;

    @Getter
    private final int scrollSteps;
    @Getter
    private final int scrollDelayMs;

    @Getter
    private final int breathSteps;
    @Getter
    private final int breathCycleMs;
    @Getter
    private final float minBrightness;

    DynamicColorEnum(Type type, int[] colorArray, int scrollSteps, int scrollDelayMs) {
        this(type, colorArray, scrollSteps, scrollDelayMs, 60, 1000, 0.2f);
    }

    DynamicColorEnum(Type type, int[] colorArray, int breathSteps, int breathCycleMs, float minBrightness) {
        this(type, colorArray, 20, 20, breathSteps, breathCycleMs, minBrightness);
    }

    DynamicColorEnum(Type type, int[] colorArray, int scrollSteps, int scrollDelayMs, int breathSteps, int breathCycleMs, float minBrightness) {
        this.type = type;
        this.colorArray = colorArray;
        this.scrollSteps = scrollSteps;
        this.scrollDelayMs = scrollDelayMs;
        this.breathSteps = breathSteps;
        this.breathCycleMs = breathCycleMs;
        this.minBrightness = minBrightness;
    }

    public Component buildNameComponent(String textKey, int romanFormatLevel, @Nullable String append, boolean isTranslatable) {
        MutableComponent baseComponent = switch (this.type) {
            case SCROLL -> DynamicComponentUtil.scrollColorfulText.getColorfulText(
                    textKey, append, this.colorArray, this.scrollSteps, this.scrollDelayMs, isTranslatable
            ).copy();
            case BREATH -> DynamicComponentUtil.BreathColorfulText.getColorfulText(
                    textKey, append, this.colorArray, this.breathSteps, this.breathCycleMs, this.minBrightness, isTranslatable
            ).copy();
            case REMIX -> DynamicComponentUtil.BreathAndScrollColorfulText.getColorfulText(
                    textKey, append, this.colorArray, this.scrollSteps, this.scrollDelayMs, this.breathSteps, this.breathCycleMs, this.minBrightness, isTranslatable
            ).copy();
        };
        if (romanFormatLevel > 0) {
            Component numeralComponent = RomanNumeralHelper.getNumeral(romanFormatLevel).copy().withStyle(style -> style.withColor(0xa1bdff));
            baseComponent.append(" ").append(numeralComponent);
        }
        return baseComponent;
    }

//    public static Component getDynamicNumeral(int value, DynamicColorEnum.Type type, int[] color, int scrollSteps, int scrollDelayMs, int breathSteps, int breathCycleMs, float minBrightness) {
//        Int2ObjectMap<Component> cache = RomanNumberHelperAccessor.getNumeralCache();
//        if (cache.containsKey(value)) {
//            return cache.get(value);
//        }
//        String key = RomanNumberHelperAccessor.getTranslationKeyPrefix() + value;
//        boolean canTranslate = Util.canTranslate(key);
//        String targetStr = canTranslate ? key : RomanNumberHelperAccessor.invokeIntToRomanNumeral(value);
//        Component component = switch (type) {
//            case SCROLL -> DynamicComponentUtil.scrollColorfulText.getColorfulText(
//                    targetStr, null, color, scrollSteps, scrollDelayMs, canTranslate
//            );
//            case BREATH -> DynamicComponentUtil.BreathColorfulText.getColorfulText(
//                    targetStr, null, color, breathSteps, breathCycleMs, minBrightness, canTranslate
//            );
//            case REMIX -> DynamicComponentUtil.BreathAndScrollColorfulText.getColorfulText(
//                    targetStr, null, color, scrollSteps, scrollDelayMs, breathSteps, breathCycleMs, minBrightness, canTranslate
//            );
//        };
//        cache.put(value, component);
//        return component;
//    }
}
