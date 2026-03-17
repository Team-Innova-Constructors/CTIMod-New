package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Entity.Projectiles.GelCloudEntity;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.forTrait.OverslimeHandler;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class SlimeExplode extends EtSTBaseModifier {
    @Override
    public int getPriority() {
        return OverslimeHandler.OVERSLIME_MODIFIER_PRIORITY+1;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        var attacker = context.getAttacker();
        var level = attacker.level;
        var target = context.getTarget();
        var targetCenter = target.position().add(0, 0.5 * target.getBbHeight(), 0);
        var os = TinkerModifiers.overslime.get();

        if (!context.isExtraAttack()) {
            if (os.getShield(tool) >= 20) {
                os.addOverslime(tool, modifier, -20);
                float percentage = 0.25f + Math.min(modifier.getLevel(), os.getShield(tool) / 4000f);
                level.getEntitiesOfClass(LivingEntity.class, new AABB(targetCenter.x - 3, targetCenter.y - 3, targetCenter.z - 3, targetCenter.x + 3, targetCenter.y + 3, targetCenter.z + 3), living ->
                        !(living instanceof Player) && living != attacker).forEach(living -> {
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.mobAttack(attacker), damage * percentage);
                    living.invulnerableTime = 0;
                });
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(CtiParticleType.GEL_EXPLOSION.get(), targetCenter.x, targetCenter.y, targetCenter.z, 1, 0, 0, 0, 0);
                    GelCloudEntity entity = new GelCloudEntity(level);
                    entity.damage = 4 * modifier.getLevel();
                    entity.radius = 3 + modifier.getLevel();
                    entity.lvl = modifier.getLevel();
                    entity.setOwner(attacker);
                    entity.setPos(targetCenter);
                    serverLevel.addFreshEntity(entity);
                }
            }
        }
    }
}
