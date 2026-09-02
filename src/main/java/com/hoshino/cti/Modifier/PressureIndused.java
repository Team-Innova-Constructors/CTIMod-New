package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

import static com.hoshino.cti.content.environmentSystem.EnvironmentalHandler.*;

public class PressureIndused extends EtSTBaseModifier implements LeftClickModifierHook , IModifierWithSpecialDesc {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (target instanceof LivingEntity living && !(target instanceof Player)) {
                living.invulnerableTime = 0;
                if (getPressureResistance(living) <= 1.5 && getPressureValue(living) < 50&&Math.random()<0.2*entry.getLevel()) {
                    addPressureValue(living, entry.getLevel());
                }
                living.invulnerableTime = 0;
            }
        }
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        Entity entity = context.getTarget();
        LivingEntity living = context.getAttacker();
        if (entity instanceof LivingEntity target && living instanceof Player player && !(entity instanceof Player)&&context.isFullyCharged()) {
            target.hurt(EDamageSource.indirectPressure(false, player,modifier.getLevel()), damageDealt / 6);
        }
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.true_melee");
    }
}
