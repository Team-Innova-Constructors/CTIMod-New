package com.hoshino.cti.Modifier.Contributors;


import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import static com.hoshino.cti.register.CtiModifiers.originbasedcomputation;

public class OriginBasedComputation extends BattleModifier {
    public OriginBasedComputation() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(this::livingcriticalhitevent);
    }

    @Override
    public boolean havenolevel() {
        return true;
    }

    private void livingcriticalhitevent(CriticalHitEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() != null && ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), originbasedcomputation.getId()) > 0) {
            if (event.getResult() != Event.Result.ALLOW) {
                event.setResult(Event.Result.ALLOW);
            }
            event.setDamageModifier(1.85F);
        }
    }

    public void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getSource().getEntity();
        if (a instanceof ServerPlayer player && event.getEntity() != null) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), originbasedcomputation.getId()) > 0) {
                event.getEntity().invulnerableTime = 0;
                event.getSource().bypassArmor().bypassMagic().bypassInvul().bypassEnchantments();
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
}