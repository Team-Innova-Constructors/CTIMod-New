package com.hoshino.cti.Modifier;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nullable;
import java.util.List;

public class CombustorModifier extends NoLevelsModifier implements MeleeDamageModifierHook, ProjectileLaunchModifierHook, ModifierRemovalHook, TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.PROJECTILE_LAUNCH, ModifierHooks.REMOVE, ModifierHooks.TOOLTIP);
    }

    public static final ResourceLocation fuel = new ResourceLocation("cti:combust_fuel");
    public static final ResourceLocation fuel_quality = new ResourceLocation("cti:fuel_quality");


    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() instanceof Player player) {
            float f = getBonus(tool);
            shrinkFuel(tool, player);
            return damage + baseDamage * f;
        }
        return damage;
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, NamespacedNBT persistentData, boolean primary) {
        if (shooter instanceof Player player) {
            float f = getBonus(tool);
            shrinkFuel(tool, player);
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(1 + f));
        }
    }

    public float getBonus(IToolStackView tool) {
        return tool.getPersistentData().getFloat(fuel_quality);
    }

    public void addFuel(IToolStackView tool, Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0 && !stack.hasCraftingRemainingItem()) {
                tool.getPersistentData().putInt(fuel, ForgeHooks.getBurnTime(stack, RecipeType.SMELTING));
                tool.getPersistentData().putFloat(fuel_quality, Math.min((float) ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) / 3200, 4.5f));
                stack.shrink(1);
                player.playSound(SoundEvents.BLAZE_SHOOT);
            }
        }
    }

    public void shrinkFuel(IToolStackView tool, Player player) {
        tool.getPersistentData().putInt(fuel, (int) (tool.getPersistentData().getInt(fuel) - tool.getPersistentData().getFloat(fuel_quality) * 50));
        if (tool.getPersistentData().getInt(fuel) <= 0) {
            tool.getPersistentData().putInt(fuel, 0);
            addFuel(tool, player);
        }
        if (tool.getPersistentData().getInt(fuel) <= 0) {
            tool.getPersistentData().putInt(fuel_quality, 0);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(fuel);
        tool.getPersistentData().remove(fuel_quality);
        return null;
    }

    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return super.getDisplayName().copy().append(" 伤害增加 +" + String.format("%.1f", getBonus(tool) * 100) + "%");
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.literal("燃料:").append(String.valueOf(tool.getPersistentData().getInt(fuel))).withStyle(this.getDisplayName().getStyle()));
    }
}
