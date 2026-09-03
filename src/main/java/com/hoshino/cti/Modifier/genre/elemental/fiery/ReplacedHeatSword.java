package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Entity.Projectiles.FierySlashProjectile;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.handler.BotaniaSounds;

public class ReplacedHeatSword extends BasicOverHeatModifier {
    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (player.hasEffect(CtiEffects.OVERHEAT.get()))
                shootSlash(tool,player,entry);
        }
        super.onLeftClickBlock(tool,entry,player,level,equipmentSlot,state,pos);
    }
    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (player.hasEffect(CtiEffects.OVERHEAT.get()))
                shootSlash(tool,player,entry);
        }
        super.onLeftClickEntity(tool,entry,player,level,equipmentSlot,target);
    }
    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (player.hasEffect(CtiEffects.OVERHEAT.get()))
                shootSlash(tool,player,entry);
        }
        super.onLeftClickEmpty(tool,entry,player,level,equipmentSlot);
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

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getAttacker() instanceof Player player&&context.getTarget() instanceof LivingEntity target&&player.hasEffect(CtiEffects.OVERHEAT.get())){
            target.invulnerableTime = 0;
            target.hurt(ElementalDamageSource.burnt(player),damage*0.2f*modifier.getLevel());
            target.invulnerableTime = 0;
        }
    }

    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel()*30;
    }

    @Override
    public float getBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel()*0.25f;
    }
}
