package com.hoshino.cti.integration.botania.api.hook;

import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.Collection;
import java.util.List;

public interface BurstDamageModifierHook {
    float getBurstDamage(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, @NotNull Entity target, ManaBurst burst,IManaBurstExtra burstExtra, float baseDamage, float damage);

    record AllMerger(Collection<BurstDamageModifierHook> modules) implements BurstDamageModifierHook {
        @Override
        public float getBurstDamage(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, @NotNull Entity target, ManaBurst burst,IManaBurstExtra burstExtra, float baseDamage, float damage) {
            for (BurstDamageModifierHook hook:this.modules){
                damage = hook.getBurstDamage(tool,modifier,modifierList,owner,target,burst,(IManaBurstExtra) burst,baseDamage,damage);
            }
            return damage;
        }
    }
}
