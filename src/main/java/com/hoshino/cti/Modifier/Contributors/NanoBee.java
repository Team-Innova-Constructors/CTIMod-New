package com.hoshino.cti.Modifier.Contributors;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class NanoBee extends NoLevelsModifier implements InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if(b1){
                this.drawParticle(serverPlayer.position(), serverPlayer.getLevel(), 5);
                if (serverPlayer.tickCount % 20 != 0) return;
                var area = new AABB(serverPlayer.getOnPos()).inflate(5);
                var livingList = serverPlayer.level.getEntitiesOfClass(LivingEntity.class, area, lv -> {
                    if (lv instanceof TamableAnimal animal && animal.isTame()) {
                        return false;
                    }
                    return lv.isAlive() && !(lv instanceof Villager) && lv != serverPlayer;
                });
                for (LivingEntity living : livingList) {
                    var source = DamageSource.indirectMagic(serverPlayer,serverPlayer);
                    living.invulnerableTime = 0;
                    living.hurt(source, living.getMaxHealth() * 0.1f);
                    boolean hasCurse=living.hasEffect(LCEffects.CURSE.get());
                    if(hasCurse)return;
                    living.addEffect(new MobEffectInstance(LCEffects.CURSE.get(),0,1000,false,false));
                }
            }
        }
    }

    public void drawParticle(Vec3 center, ServerLevel serverLevel, double radius) {
        for (int i = 0; i < 36; i++) {
            double angle = (2 * Math.PI / 36) * i;
            double x = center.x + radius * Math.cos(angle);
            double y = center.y;
            double z = center.z + radius * Math.sin(angle);
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0.15, 0, 0);
        }
    }
}
