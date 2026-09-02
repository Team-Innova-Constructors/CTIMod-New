package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.content.environmentSystem.EnvironmentalHandler;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class IonizeIndused extends EtSTBaseModifier implements LeftClickModifierHook , IModifierWithSpecialDesc {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    @Override
    public int getPriority() {
        return 255;
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (target instanceof LivingEntity living && !(target instanceof Player)) {
                living.invulnerableTime = 0;
                living.hurt(EDamageSource.indirectIonize(false, player,entry.getLevel()), (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE)/2);
                if (EnvironmentalHandler.getIonizeResistance(living) <= 1.5 &&EnvironmentalHandler.getIonizeValue(living) < 50) {
                    EnvironmentalHandler.addIonizeValue(living, 4 * entry.getLevel());
                }
                living.invulnerableTime = 0;
            }
        }
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.true_melee");
    }
}
