package com.hoshino.cti.Modifier.Contributors;


import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import static com.hoshino.cti.register.CtiModifiers.originbasedcomputation;

public class OriginBasedComputation extends BattleModifier {
    public OriginBasedComputation() {
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingCriticalHit);
    }

    @Override
    public boolean havenolevel() {
        return true;
    }

    private void OnLivingCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (player!=null && event.getTarget() != null && ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), originbasedcomputation.getId()) > 0) {
            if (event.getResult() != Event.Result.ALLOW) {
                event.setResult(Event.Result.ALLOW);
            }
            event.setDamageModifier(event.getDamageModifier()+0.35F);
        }
    }

    @Override
    public void LivingAttackEvent(LivingAttackEvent event) {
        Entity a = event.getSource().getEntity();
        if (a instanceof Player player) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), originbasedcomputation.getId()) > 0) {
                event.getEntity().invulnerableTime = 0;
                event.getSource().bypassArmor().bypassMagic().bypassInvul().bypassEnchantments();
            }
        }
    }
}