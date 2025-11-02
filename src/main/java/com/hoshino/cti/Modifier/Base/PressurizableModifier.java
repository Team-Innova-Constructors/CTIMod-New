package com.hoshino.cti.Modifier.Base;

import com.hoshino.cti.Modifier.capability.PressurizableToolCap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

import static com.hoshino.cti.Modifier.capability.PressurizableToolCap.*;

public class PressurizableModifier extends Modifier implements VolatileDataModifierHook, ModifierRemovalHook, TooltipModifierHook, ValidateModifierHook {
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this,ModifierHooks.VOLATILE_DATA, ModifierHooks.REMOVE,ModifierHooks.TOOLTIP,ModifierHooks.VALIDATE);
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        if (modDataNBT.contains(BASE_VOLUME_KEY, 3)) {
            modDataNBT.putInt(BASE_VOLUME_KEY, modDataNBT.getInt(BASE_VOLUME_KEY) + this.getBaseVolume(modifierEntry));
        } else {
            modDataNBT.putInt(BASE_VOLUME_KEY, this.getBaseVolume(modifierEntry));
        }
        if (modDataNBT.contains(MAX_PRESSURE_KEY, 3)) {
            modDataNBT.putFloat(MAX_PRESSURE_KEY, modDataNBT.getInt(MAX_PRESSURE_KEY) + this.getMaxPressure(modifierEntry));
        } else {
            modDataNBT.putFloat(MAX_PRESSURE_KEY, this.getMaxPressure(modifierEntry));
        }
    }

    public int getBaseVolume(ModifierEntry entry) {
        return 10000;
    }

    public float getMaxPressure(ModifierEntry entry) {
        return 20f;
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        if (iToolStackView.getVolatileData().getInt(BASE_VOLUME_KEY) <= 0) {
            iToolStackView.getPersistentData().remove(AIR_KEY);
        }
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("cti.tooltip.modifier.volume").append(":").append(" " + tool.getVolatileData().getInt(BASE_VOLUME_KEY) + "mL"));
        list.add(Component.translatable("cti.tooltip.modifier.air").append(":").append(" " + tool.getPersistentData().getInt(AIR_KEY) + "mL"));
        list.add(Component.translatable("cti.tooltip.modifier.pressure").append(":").append(" " + String.format("%.1f", (float) tool.getPersistentData().getInt(AIR_KEY) / tool.getVolatileData().getInt(BASE_VOLUME_KEY)) + "/" + String.format("%.1f", tool.getVolatileData().getFloat(MAX_PRESSURE_KEY)) + "bar"));
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifierEntry) {
        if (getAir(tool)> PressurizableToolCap.getMaxPressure(tool)*PressurizableToolCap.getBaseVolume(tool)) addAir(tool, (int) (PressurizableToolCap.getMaxPressure(tool)*PressurizableToolCap.getBaseVolume(tool)-getAir(tool)));
        return null;
    }
}
