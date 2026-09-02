package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.aetherteam.aether.client.AetherSoundEvents;
import com.c2h6s.etshtinker.Modifiers.modifiers.etshmodifierfluxed;
import com.hoshino.cti.Entity.Projectiles.RubyLaserEntity;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class RubyLaserModifier extends BasicOverHeatModifier {
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 350*modifierEntry.getLevel();
    }

    @Override
    public float getBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel()*0.5F;
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        var toolData = tool.getPersistentData();
        boolean b = player.getAttackStrengthScale(0)>0.8&&!level.isClientSide;
        if (b){
            var playerTickers = EntityTickerManager.getInstance(player);
            if (!playerTickers.hasTicker(CtiEntityTickers.RUBY_LASER_CD.get())&&!player.hasEffect(CtiEffects.OVERHEAT.get())){
                toolData.putInt(ReplacedHeatSword.KEY_HEAT,Math.min(100, toolData.getInt(ReplacedHeatSword.KEY_HEAT)+entry.getLevel()*5));
                playerTickers.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.RUBY_LASER_CD.get(), 1,20));
            }
        }
        super.onLeftClickEntity(tool, entry, player, level, equipmentSlot, target);
        if (b&& etshmodifierfluxed.removeEnergy(tool,5000,true,true)){
            etshmodifierfluxed.removeEnergy(tool,5000,false,true);
            shootRubyLaser(player,tool,entry);
        }
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        super.onLeftClickEmpty(tool, entry, player, level, equipmentSlot);
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide&& etshmodifierfluxed.removeEnergy(tool,5000,true,true)){
            etshmodifierfluxed.removeEnergy(tool,5000,false,true);
            shootRubyLaser(player,tool,entry);
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        super.onLeftClickBlock(tool, entry, player, level, equipmentSlot, state, pos);
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide&& etshmodifierfluxed.removeEnergy(tool,5000,true,true)){
            etshmodifierfluxed.removeEnergy(tool,5000,false,true);
            shootRubyLaser(player,tool,entry);
        }
    }

    public static void shootRubyLaser(Player player, IToolStackView tool, ModifierEntry entry){
        var laser = new RubyLaserEntity(CtiEntity.RUBY_LASER.get(),player.getLevel());
        laser.setPos(player.getEyePosition());
        laser.setDeltaMovement(player.getLookAngle());
        laser.setDataLength(8+entry.getLevel()*4);
        laser.burnt = 10*entry.getLevel();
        laser.setOwner(player);
        laser.tool = (ToolStack) tool;
        player.getLevel().addFreshEntity(laser);
        player.level.playSound(null,player, Sounds.CRYSTALSHOT.getSound(), SoundSource.PLAYERS,1,1);
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.burnt","info.cti.true_melee");
    }
}
