package com.hoshino.cti.Modifier.Contributors;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import java.util.UUID;


public class Nkssdtt extends NoLevelsModifier implements InventoryTickModifierHook{
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    public static final ResourceLocation NKSSTK = Cti.getResource("nkssdtt_target");
    public static final ResourceLocation NKSSTK_COOLDOWN = Cti.getResource("nkssdtt_cooldown");
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level1, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if (!(livingEntity instanceof Player player)) return;
        var data = iToolStackView.getPersistentData();
        var time = player.getPersistentData().getInt("nksswait");
        var string = data.getString(NKSSTK);
        var cooldownTime=data.getInt(NKSSTK_COOLDOWN);
        if(cooldownTime>0&&player.tickCount%20==0){
            data.putInt(NKSSTK_COOLDOWN,cooldownTime-1);
        }
        if(time==5){
            player.setDeltaMovement(new Vec3(0,2,0));
        }
        if (!b1) return;
        if (time > 1) {
            player.getPersistentData().putInt("nksswait", time - 1);
            return;
        } else if (time == 1) {
            player.setDeltaMovement(0, -2, 0);
            player.getPersistentData().putInt("nksswait", 0);
            return;
        }
        if (string.isEmpty()) return;
        var uuid = UUID.fromString(string);
        if (level1 instanceof ServerLevel level) {
            var entity = level.getEntities().get(uuid);
            if (entity instanceof LivingEntity lv) {
                if (lv.isAlive()) {
                    ToolAttackUtil.attackEntity(itemStack,player,entity);
                    lv.hurt(DamageSource.playerAttack(player),lv.getMaxHealth() * 0.4f);
                    SoundEvent events;
                    boolean c = player.level.random.nextBoolean();
                    if (c) {
                        events = CtiSounds.superDie1.get();
                    } else events = CtiSounds.superDie2.get();
                    player.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), events, SoundSource.AMBIENT, 1, 1);
                    player.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.AMBIENT, 1, 1);
                    level.sendParticles(ParticleTypes.CRIT, entity.getX(), entity.getY(0.5), entity.getZ(), 10, 0.1, 0, 0.1, 0.2);
                    level.sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(0.5), entity.getZ(), 20, 0.1, 0, 0.1, 0.2);
                    data.remove(NKSSTK);
                    player.swing(InteractionHand.MAIN_HAND, true);
                } else data.remove(NKSSTK);
            }
        }
    }
}
