package com.hoshino.cti.Modifier.Developer;

import com.hoshino.cti.Modifier.StarDragonHit;
import com.hoshino.cti.register.CtiEffects;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Eventually extends BattleModifier {
    @Override
    public void LivingAttackEvent(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (ModifierUtil.getModifierLevel(player.getMainHandItem(), this.getId()) > 0) {
                event.getSource().bypassMagic().bypassInvul().bypassArmor();
            }
        }
    }

    @Override
    public int getPriority() {
        return 600;
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (context.getLivingTarget() instanceof Player) {
            return damage;
        } else if (attacker.hasEffect(CtiEffects.ev.get())) {
            return Float.MAX_VALUE;
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var target=context.getLivingTarget();
        var player=context.getPlayerAttacker();
        if (target!= null && player!= null) {
            if (target instanceof Player) {
                return;
            }
            StarDragonHit.runSpecialKill(target,player);
        }
    }
}