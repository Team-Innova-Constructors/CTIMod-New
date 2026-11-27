package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Entity.Projectiles.FierySlashProjectile;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.handler.BotaniaSounds;

public class FierySlashModifier extends BasicBurntModifier implements LeftClickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 25*modifierEntry.getLevel();
    }

    @Override
    public int getReleasedBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 75*modifierEntry.getLevel();
    }

    @Override
    public float getReleasedBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 0.25f*modifierEntry.getLevel();
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8f&&player.hasEffect(CtiEffects.OVERHEAT.get())&&!level.isClientSide){
            shootSlash(tool,player,entry);
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.8f&&player.hasEffect(CtiEffects.OVERHEAT.get())&&!level.isClientSide){
            shootSlash(tool,player,entry);
        }
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player.getAttackStrengthScale(0)>0.8f&&player.hasEffect(CtiEffects.OVERHEAT.get())&&!level.isClientSide){
            shootSlash(tool,player,entry);
        }
    }

    public static void shootSlash(IToolStackView tool, Player shooter, ModifierEntry modifier){
        var fierySlash = new FierySlashProjectile(CtiEntity.FIERY_SLASH.get(),shooter.getLevel());
        fierySlash.setDeltaMovement(shooter.getLookAngle().scale(7f));
        fierySlash.setOwner(shooter);
        fierySlash.tool = tool;
        fierySlash.burntLevel = modifier.getLevel();
        fierySlash.setPos(shooter.position().add(0,shooter.getBbHeight()/2,0));
        shooter.level.addFreshEntity(fierySlash);
        shooter.level.playSound(null,shooter, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,1,0.75f);
        shooter.level.playSound(null,shooter, BotaniaSounds.endoflame, SoundSource.PLAYERS,2,1.5f);
    }
}
