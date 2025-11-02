package com.hoshino.cti.Modifier.Developer;

import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiToolStats;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public class Trauma extends ArmorModifier {
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        CtiToolStats.ELECTRIC_RESISTANCE.add(builder, 14.5F);
        CtiToolStats.FROZEN_RESISTANCE.add(builder, 14.5F);
        CtiToolStats.PRESSURE_RESISTANCE.add(builder, 14.5F);
        CtiToolStats.SCORCH_RESISTANCE.add(builder, 14.5F);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player) {
            if (player.hasEffect(CtiEffects.ev.get())) {
                if (ModifierLevel.EquipHasModifierlevel(entity, this.getId())) {
                    double x = player.getX();
                    double y = player.getY();
                    double z = player.getZ();
                    List<Mob> mobbbb = player.level.getEntitiesOfClass(Mob.class, new AABB(x + 10, y + 10, z + 10, x - 10, y - 10, z - 10));
                    for (Mob targets : mobbbb) {
                        if (targets != null && !targets.getType().getCategory().isFriendly()&&targets.tickCount>20) {
                            if (targets instanceof Slime slime) {
                                slime.setSize(1, false);
                            }
                            BlockPos posA = player.getOnPos();
                            targets.hurt(DamageSource.playerAttack(player).bypassMagic().bypassArmor().bypassInvul(), Float.MAX_VALUE);
                            targets.die(DamageSource.playerAttack(player));
                            player.level.playSound(null, posA, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1F, 1F);
                            targets.remove(Entity.RemovalReason.KILLED);
                            if(player instanceof ServerPlayer serverPlayer){
                                serverPlayer.getLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, targets.getX(), targets.getY()+targets.getBbHeight(), targets.getZ(), 40, 0.25, 0.5, 0.25, 0.25);
                            }
                            if (player.level.isClientSide && mobbbb.size() < 20) {
                                Vec3 center = player.position();
                                float tpi = (float) (Math.PI * 2);
                                Vec3 v0 = new Vec3(0, 10, 0);
                                v0 = v0.xRot(tpi / 4).yRot(player.getRandom().nextFloat() * tpi);
                                player.level.addAlwaysVisibleParticle(ParticleTypes.FLAME,
                                        center.x + v0.x,
                                        center.y + v0.y + 0.5f,
                                        center.z + v0.z, 0, 0, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}