package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.util.slotUtil;
import com.hoshino.cti.Modifier.Base.PressurizableModifier;
import com.hoshino.cti.Modifier.capability.PressurizableToolCap;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.hoshino.cti.Modifier.capability.PressurizableToolCap.AIR_KEY;

public class PressureLoaded extends PressurizableModifier implements ToolDamageModifierHook, BreakSpeedModifierHook, MeleeDamageModifierHook, ProjectileLaunchModifierHook, AttributesModifierHook, DurabilityDisplayModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.BREAK_SPEED, ModifierHooks.MELEE_DAMAGE, ModifierHooks.PROJECTILE_LAUNCH, ModifierHooks.ATTRIBUTES, ModifierHooks.DURABILITY_DISPLAY);
    }

    @Override
    public int getBaseVolume(ModifierEntry entry) {
        return 5000 * entry.getLevel();
    }

    @Override
    public float getMaxPressure(ModifierEntry entry) {
        return Math.min(20, 5 * entry.getLevel());
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (100 * amount < PressurizableToolCap.getAir(tool)) {
            PressurizableToolCap.addAir(tool, -100 * amount);
            return 0;
        } else if (PressurizableToolCap.getAir(tool) > 100) {
            int minus = PressurizableToolCap.getAir(tool) / 100;
            PressurizableToolCap.addAir(tool, -100 * minus);
            return amount - minus;
        }
        return amount;
    }

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event, Direction direction, boolean isEffective, float miningSpeedModifier) {
        Optional<BlockPos> pos = event.getPosition();
        if (!isEffective || pos.isEmpty()) {
            return;
        }
        event.setNewSpeed(event.getNewSpeed() * (1 + (float) tool.getPersistentData().getInt(AIR_KEY) * modifier.getLevel() / 2));
    }

    public float getBonus(IToolStackView tool, ModifierEntry modifier) {
        return PressurizableToolCap.getPressure(tool) > 0 ? (PressurizableToolCap.getPressure(tool) + modifier.getLevel()) / 40 : 0;
    }


    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        float multiplier = 1 + getBonus(tool, modifier);
        if (PressurizableToolCap.getAir(tool) > 100) {
            PressurizableToolCap.addAir(tool, -(int) (100 * multiplier));
            return damage * multiplier;
        }
        return damage;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        super.addTooltip(tool, modifierEntry, player, list, tooltipKey, tooltipFlag);
        list.add(Component.translatable("cti.tooltip.modifier.pressure_damage").append(" +").append(String.format("%.1f", getBonus(tool, modifierEntry) * 100)).append("%").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean b) {
        float multiplier = 1 + getBonus(tool, modifier);
        if (PressurizableToolCap.getAir(tool) > 100 && projectile instanceof AbstractArrow arrow) {
            PressurizableToolCap.addAir(tool, -(int) (100 * multiplier));
            arrow.setBaseDamage(arrow.getBaseDamage() * multiplier);
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        if (slotUtil.ARMOR.contains(equipmentSlot) && getBonus(tool, modifier) > 0) {
            biConsumer.accept(Attributes.ARMOR, new AttributeModifier(CommonUtil.UUIDFromSlot(equipmentSlot,modifier.getId()), Attributes.ARMOR.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
            biConsumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(CommonUtil.UUIDFromSlot(equipmentSlot,modifier.getId()), Attributes.ARMOR_TOUGHNESS.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
            biConsumer.accept(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(CommonUtil.UUIDFromSlot(equipmentSlot,modifier.getId()), Attributes.KNOCKBACK_RESISTANCE.getDescriptionId(), getBonus(tool, modifier), AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Nullable
    @Override
    public Boolean showDurabilityBar(IToolStackView tool, ModifierEntry modifierEntry) {
        return PressurizableToolCap.getAir(tool) > 0;
    }

    @Override
    public int getDurabilityWidth(IToolStackView tool, ModifierEntry modifierEntry) {
        return (int) (PressurizableToolCap.getAir(tool) * 12 / (PressurizableToolCap.getMaxPressure(tool) * PressurizableToolCap.getBaseVolume(tool))) + 1;
    }

    @Override
    public int getDurabilityRGB(IToolStackView iToolStackView, ModifierEntry modifierEntry) {
        return 0xffffff;
    }
}
