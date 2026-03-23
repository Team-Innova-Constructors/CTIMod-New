package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.defense;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class ReplaceOverlostArmor extends BasicOverslimeModifier implements ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.MODIFY_HURT);
    }

    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*100;
    }

    @Override
    public float getArmorMul(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*0.10f;
    }

    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return 2;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.getEntity() instanceof LivingEntity living){
            var slowEffect = living.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
            if (slowEffect!=null){
                var boost = slowEffect.getAmplifier()+1;
                boost=Math.min(boost,10);
                var os = TinkerModifiers.overslime.get();
                if (os.getShield(tool)>=boost){
                    os.addOverslime(tool,modifier,-boost);
                    amount-=boost*10;
                }
            }
        }
        return amount;
    }
}
