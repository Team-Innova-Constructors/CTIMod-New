package com.hoshino.cti.Modifier.aetherCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.OnHoldingPreventDeathHook;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Reborn extends EtSTBaseModifier implements OnHoldingPreventDeathHook {
    public static final ResourceLocation KEY_CD = Cti.getResource("reborn_cd");
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.PREVENT_DEATH);
    }

    @Override
    public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
        if (tool.getPersistentData().getInt(KEY_CD)<=0) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,4));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,200,10));
            tool.getPersistentData().putInt(KEY_CD,240);
            return livingEntity.getMaxHealth()/2;
        }
        return 0;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (level.getGameTime()%20==0&&tool.getPersistentData().getInt(KEY_CD)>0) tool.getPersistentData().putInt(KEY_CD,tool.getPersistentData().getInt(KEY_CD)-1);
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return super.getDisplayName().copy().append(" CD:"+tool.getPersistentData().getInt(KEY_CD));
    }
}
