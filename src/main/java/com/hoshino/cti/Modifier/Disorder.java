package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Entities.plasmaexplosionentity;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerEffects;
import com.c2h6s.etshtinker.init.etshtinkerEntity;
import com.c2h6s.etshtinker.init.etshtinkerParticleType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;
import static com.c2h6s.etshtinker.util.vecCalc.getScatteredVec3;

public class Disorder extends EtSTBaseModifier {
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (!context.isExtraAttack()) {
            Player player = context.getPlayerAttacker();
            Entity entity = context.getTarget();
            Random random = new Random();
            if (player != null && entity instanceof LivingEntity target && random.nextInt(5) <= modifier.getLevel()) {
                int i = random.nextInt(16);
                Level level = player.level;
                Vec3 vec3 = getScatteredVec3(new Vec3(0, EtSHrnd().nextInt(2) == 1 ? 1 : -1, 0), 80).normalize();
                double d = EtSHrnd().nextDouble() * 1 + 2;
                Vec3 direction = new Vec3(-(d) * vec3.x, -(d) * vec3.y, -(d) * vec3.z);
                plasmaexplosionentity explosion = new plasmaexplosionentity(etshtinkerEntity.plasmaexplosionentity.get(), level);
                explosion.special = "entropic";
                explosion.damage = damage * i * 0.25f;
                explosion.particle = etshtinkerParticleType.plasmaexplosionpurple.get();
                explosion.setPos(new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ()).add(direction));
                explosion.rayVec3 = vec3.scale(d * 2);
                level.addFreshEntity(explosion);
                if (random.nextInt(10) <= modifier.getLevel()) {
                    vec3 = getScatteredVec3(new Vec3(0, EtSHrnd().nextInt(2) == 1 ? 1 : -1, 0), 80).normalize();
                    d = EtSHrnd().nextDouble() * 16 + 16;
                    explosion.special = "entropic";
                    direction = new Vec3(-(d) * vec3.x, -(d) * vec3.y, -(d) * vec3.z);
                    explosion = new plasmaexplosionentity(etshtinkerEntity.plasmaexplosionentity.get(), level);
                    explosion.damage = damage * 8 / i;
                    explosion.particle = etshtinkerParticleType.plasmaexplosionpurple.get();
                    explosion.setPos(new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ()).add(direction));
                    explosion.rayVec3 = vec3.scale(d * 2);
                    level.addFreshEntity(explosion);
                }
            }
        }
        if (context.getTarget() instanceof LivingEntity living) {
            living.forceAddEffect(new MobEffectInstance(etshtinkerEffects.hi_gravity.get(), 200, 1, false, false), context.getAttacker());
        }
        return knockback;
    }
}
