package com.hoshino.cti.Modifier.Developer;

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
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        List<Entity> entities = entity.level.getEntitiesOfClass(Entity.class, new AABB(entity.getX() + 10, entity.getY() + 10, entity.getZ() + 10, entity.getX() - 10, entity.getY() - 10, entity.getZ() - 10));
        for (Entity entity1 : entities) {
            if (entity1 instanceof ExperienceOrb exp) {
                exp.moveTo(entity.getPosition(1));
                return;
            }
        }
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (context.getLivingTarget() instanceof Player) {
            return damage;
        } else if (attacker.hasEffect(CtiEffects.ev.get())) {
            return damage + Float.MAX_VALUE;
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget() != null && context.getPlayerAttacker() != null) {
            if (context.getLivingTarget() instanceof Player) {
                return;
            } else if (context.getLivingTarget().getMaxHealth() == 40) {
                return;
            } else context.getLivingTarget().die(DamageSource.playerAttack(context.getPlayerAttacker()));
            context.getLivingTarget().remove(Entity.RemovalReason.KILLED);
        }
    }
}