package com.hoshino.cti.Modifier.Armor;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Modifier.Base.EffectsImmuneModifier;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import java.util.Collection;
import java.util.List;

public class AntiCurse extends EffectsImmuneModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_ANTI_CURSE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("key_anti_curse"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(KEY_ANTI_CURSE,false, TinkerTags.Items.HELD));
    }

    @Override
    public @NotNull Collection<MobEffect> getImmuneEffects() {
        return List.of(
                LCEffects.CURSE.get(),
                LCEffects.FLAME.get()
        );
    }
}
