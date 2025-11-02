package com.hoshino.cti.Modifier.Contributors;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class TimeToJudge extends BattleModifier {


    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget() instanceof Mob mob) {
            if (!mob.getTags().contains("wick")) {
                mob.addTag("wick");
            }
        }
    }

    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        if (target instanceof Mob mob) {
            if (!mob.getTags().contains("wick")) {
                mob.addTag("wick");
            }
        }
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() != null && event.getEntity().getTags().contains("wick")) {
            event.setAmount(event.getAmount() * 4f);
        }
    }

    @Override
    public void LivingAttackEvent(LivingAttackEvent event) {
        if (event.getEntity() != null && event.getEntity().getTags().contains("wick")) {
            event.getSource().bypassInvul().bypassMagic();
        }
    }
}
