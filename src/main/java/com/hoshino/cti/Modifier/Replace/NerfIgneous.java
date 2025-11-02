package com.hoshino.cti.Modifier.Replace;

import com.james.tinkerscalibration.TinkersCalibration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.ReduceToolDamageModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;

import javax.annotation.Nonnull;
import java.util.List;

import static slimeknights.tconstruct.library.tools.stat.ToolStats.DRAW_SPEED;
import static slimeknights.tconstruct.library.tools.stat.ToolStats.PROJECTILE_DAMAGE;

public class NerfIgneous extends Modifier implements ModifierRemovalHook, TooltipModifierHook, ToolDamageModifierHook, MeleeDamageModifierHook, BreakSpeedModifierHook, InventoryTickModifierHook, ConditionalStatModifierHook {

    private final ResourceLocation KEY = new ResourceLocation(TinkersCalibration.MODID, "igneous_mod");
    private static final Component UNBREAKING = TConstruct.makeTranslation("modifier", "igneous.unbreaking");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.BREAK_SPEED, ModifierHooks.INVENTORY_TICK, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.TOOLTIP, ModifierHooks.TOOL_DAMAGE);
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        ModDataNBT persistentData = tool.getPersistentData();
        if (persistentData.contains(KEY, 5)) {
            float value = persistentData.getFloat(KEY);
            return (float) (damage * (1 + value * 0.1 * modifier.getLevel()));
        }
        return damage;
    }

    @Override
    public void onBreakSpeed(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull PlayerEvent.BreakSpeed event, @Nonnull Direction sideHit, boolean isEffective, float miningSpeedModifier) {
        ModDataNBT persistentData = tool.getPersistentData();
        if (persistentData.contains(KEY, 5)) {
            float value = persistentData.getFloat(KEY);
            event.setNewSpeed((float) (event.getOriginalSpeed() * (1 + value * 0.1 * modifier.getLevel())));
        }
    }

    @Override
    public int getPriority() {
        return 120;
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @javax.annotation.Nullable LivingEntity holder) {
        ModDataNBT persistentData = tool.getPersistentData();
        if (persistentData.contains(KEY, 5)) {
            float value = persistentData.getFloat(KEY);
            if (value >= modifier.getLevel() / 5f) {
                return ReduceToolDamageModule.reduceDamage(amount, (float) (0.1 * modifier.getLevel()));
            } else {
                return ReduceToolDamageModule.reduceDamage(amount, value / 10);
            }
        }
        return amount;
    }

    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (tool.getModifierLevel(this.getId()) == 0) {
            tool.getPersistentData().remove(KEY);
        }
        return null;
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
        ModDataNBT persistentData = tool.getPersistentData();
        if (persistentData.contains(KEY, 5)) {
            float value = persistentData.getFloat(KEY);
            if (stat == DRAW_SPEED) {
                return (float) (baseValue * (1 + value * 0.1 * modifier.getLevel()));
            }
            if (stat == PROJECTILE_DAMAGE) {
                return (float) (baseValue * (1 + value * 0.1 * modifier.getLevel()));
            }
        }
        return baseValue;
    }

    @Override
    public void onInventoryTick(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull Level world, @Nonnull LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT persistentData = tool.getPersistentData();
        if (!world.isClientSide && holder.tickCount % 100 == 0 && isSelected && holder.isOnFire()) {
            if (persistentData.getFloat(KEY) <= 2 && RANDOM.nextFloat() <= 0.6f * modifier.getLevel()) {
                persistentData.putFloat(KEY, persistentData.getFloat(KEY) + 0.1f);
            }
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, slimeknights.mantle.client.TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            ModDataNBT persistentData = tool.getPersistentData();
            boolean harvest = tool.hasTag(TinkerTags.Items.HARVEST);
            if (persistentData.contains(KEY, 5)) {
                float value = persistentData.getFloat(KEY);
                TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable("modifier.tinkerscalibration.modifier.igneous.attack_damage"), value * 0.1 * modifier.getLevel(), tooltip);
                if (harvest) {
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable("modifier.tinkerscalibration.modifier.igneous.mining_speed"), value * 0.1 * modifier.getLevel(), tooltip);
                } else {
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable("modifier.tinkerscalibration.modifier.igneous.draw_speed"), value * 0.1 * modifier.getLevel(), tooltip);
                }
                if (value >= modifier.getLevel()) {
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), UNBREAKING, 0.1 * modifier.getLevel(), tooltip);

                } else if (value < modifier.getLevel()) {
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), UNBREAKING, value * modifier.getLevel() / 10, tooltip);
                }
                tooltip.add(Component.translatable("modifier.tinkerscalibration.igneous.cap").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }
}