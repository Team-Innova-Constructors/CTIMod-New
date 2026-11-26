package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.concurrent.atomic.AtomicInteger;

public class ThermalUnstable extends BasicBurntModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (!context.isCritical() || !(context.getAttacker() instanceof Player player) || !(context.getTarget() instanceof LivingEntity target)) return;
        var manager = EntityTickerManager.getInstancePlayerSpecific(target, player.getUUID());
        EntityTickerInstance fiery = manager.getTicker(CtiEntityTickers.FIERY.get());
        if (fiery == null || fiery.level <= 0) return;
        int consumed = Math.min(fiery.level, Math.max(1, (int) (fiery.level * 0.25f)));
        fiery.level -= consumed;
        AtomicInteger elementalReleaseLv = new AtomicInteger(0);
        player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE, 0)));
        float explosionDamage = consumed * GenreManager.calculateElementalRelease(elementalReleaseLv.get(), 20f, 2f);
        Vec3 center = target.position().add(0, target.getBbHeight() * 0.5, 0);
        AABB aabb = new AABB(center.x - 2.5, center.y - 2.5, center.z - 2.5, center.x + 2.5, center.y + 2.5, center.z + 2.5);
        player.level.getEntitiesOfClass(LivingEntity.class, aabb).stream().filter(living -> !(living instanceof Player)).forEach(living -> {
            int invulnerableTime = living.invulnerableTime;
            living.invulnerableTime = 0;
            living.hurt(ElementalDamageSource.fiery(player), explosionDamage);
            living.invulnerableTime = invulnerableTime;
        });
        if (player.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y, center.z, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(CtiParticleType.RED_SPARK.get(), center.x, center.y, center.z, 12, 0, 0, 0, 0.25);
        }
        player.level.playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}
