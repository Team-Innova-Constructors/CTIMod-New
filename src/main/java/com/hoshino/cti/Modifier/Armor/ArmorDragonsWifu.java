package com.hoshino.cti.Modifier.Armor;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ElytraFlightModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.c2h6s.etshtinker.util.vecCalc.getMold;

public class ArmorDragonsWifu extends NoLevelsModifier implements ElytraFlightModifierHook, DamageBlockModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.ELYTRA_FLIGHT);
    }

    @Override
    public boolean elytraFlightTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, LivingEntity livingEntity, int flightTicks) {
        if (livingEntity instanceof Player player && player.isShiftKeyDown()) {
            double speed = getMold(player.getDeltaMovement());
            double scale = Math.min(60 / speed, 1.2);
            player.setDeltaMovement(player.getLookAngle().scale(scale));
        }
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 3, false, false));
        return false;
    }


    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount) {
        return source == DamageSource.DRAGON_BREATH;
    }
}
