package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.client.CtiParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.combat.MeleeAttributeModule;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class IonApocalypse extends EtSTBaseModifier {
    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    public static boolean cache = true;

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        return knockback;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getTarget().level instanceof ServerLevel serverLevel&&context.isFullyCharged()) {
            if (cache) {
                cache = false;
                context.getTarget().level.getEntitiesOfClass(LivingEntity.class,new AABB(context.getTarget().blockPosition()).inflate(6),living -> living!=context.getAttacker()&&living.distanceTo(context.getTarget())<=4).forEach(living -> {
                    ToolAttackContext attackContext = new ToolAttackContext(context.getAttacker(),context.getPlayerAttacker(),context.getHand(),context.getSlotType(),living,living,context.isCritical(),context.getCooldown(),true);
                    tool.getModifierList().forEach(modifierEntry -> {
                        MeleeHitModifierHook hook = modifierEntry.getHook(ModifierHooks.MELEE_HIT);
                        hook.afterMeleeHit(tool, modifier, attackContext, 0);
                        hook.beforeMeleeHit(tool, modifier, attackContext, damage, 0, 0);
                        hook.afterMeleeHit(tool, modifier, attackContext, damage);
                    });
                });
                serverLevel.sendParticles(CtiParticleType.ION.get(),
                        context.getTarget().getX(),
                        context.getTarget().getY()+context.getTarget().getBbHeight()/2,
                        context.getTarget().getZ(),
                        25, 0,0,0,1.5d);
                serverLevel.sendParticles(ParticleTypes.FLASH,
                        context.getTarget().getX(),
                        context.getTarget().getY()+context.getTarget().getBbHeight()/2,
                        context.getTarget().getZ(),
                        1, 0,0,0,0);
                cache = true;
            }
        }
    }
}
