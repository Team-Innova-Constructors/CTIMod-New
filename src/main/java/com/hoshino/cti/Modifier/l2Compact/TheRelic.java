package com.hoshino.cti.Modifier.l2Compact;

import com.hoshino.cti.util.DynamicColorEnum;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class TheRelic extends Modifier {
    public @NotNull Component getDisplayName(int level) {
        return DynamicColorEnum.THE_RELIC.buildNameComponent(getTranslationKey(),level,null, true);
    }
}
