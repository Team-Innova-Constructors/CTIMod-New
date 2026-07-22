package com.hoshino.cti.Modifier.l2Compact;

import com.hoshino.cti.util.DynamicColorEnum;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class ShadowOfVigrid extends Modifier {
    public @NotNull Component getDisplayName(int level) {
        return DynamicColorEnum.SHADOW_OF_VIGRID.buildNameComponent(getTranslationKey(),level,null, true);
    }
}
