package com.hoshino.cti.Modifier.genre.insatiable.forTrait;

import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.util.CommonUtil;
import com.hoshino.cti.util.MathUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class InsatiableHandler extends Modifier implements ProjectileHitModifierHook, MeleeHitModifierHook, TooltipModifierHook, ModifyDamageModifierHook, AttributesModifierHook {
    public static final ToolType[] TYPES = {ToolType.MELEE, ToolType.RANGED};
    public static final int PRIORITY = 200;
    public static final int EFFECT_TICKS = 300;

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    public static int getMaxInsatiable(LivingEntity living){
        var instance = living.getAttribute(CtiAttributes.MAX_INSATIABLE.get());
        if (instance==null) return 0;
        return (int) instance.getValue();
    }


    public static void applyEffect(LivingEntity living, ToolType type, int duration, int add) {
        TinkerEffect effect = TinkerModifiers.insatiableEffect.get(type);
        effect.apply(living, duration, Math.min(getMaxInsatiable(living), effect.getLevel(living) + add), true);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, ModifierHooks.MELEE_HIT, ModifierHooks.TOOLTIP,ModifierHooks.MODIFY_DAMAGE,ModifierHooks.ATTRIBUTES);
    }


    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (!context.isExtraAttack() && context.isFullyCharged()) {
            applyEffect(context.getAttacker(), ToolType.MELEE, EFFECT_TICKS, modifier.getLevel());
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker != null) {
            applyEffect(attacker, ToolType.RANGED, EFFECT_TICKS, modifier.getLevel());
        }
        return false;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey key, TooltipFlag tooltipFlag) {
        Optional.of(GenreManager.INSATIABLE_GENRE.baseStat).ifPresent(stat->{
            tooltip.add(Component.translatable(stat.getTranslationKey()).append("+"+tool.getStats().getInt(stat)).withStyle(style -> style.withColor(0x9546C9)));
        });
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        applyEffect(context.getEntity(),ToolType.ARMOR,EFFECT_TICKS,modifier.getLevel());
        return amount;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        consumer.accept(CtiAttributes.MAX_INSATIABLE.get(),new AttributeModifier(
                CommonUtil.UUIDFromSlot(slot,modifier.getId()),CtiAttributes.MAX_INSATIABLE.get().getDescriptionId(),
                tool.getStats().get(GenreManager.INSATIABLE_GENRE.baseStat), AttributeModifier.Operation.ADDITION
        ));
    }
}