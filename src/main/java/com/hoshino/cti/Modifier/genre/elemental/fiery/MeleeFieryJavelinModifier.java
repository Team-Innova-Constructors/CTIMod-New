package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Entity.Projectiles.MeleeFieryJavelinProjectile;
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
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class MeleeFieryJavelinModifier extends BasicOverHeatModifier{
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 75;
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide)
            shootJavelin(player, (ToolStack) tool);
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide)
            shootJavelin(player, (ToolStack) tool);
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide)
            shootJavelin(player, (ToolStack) tool);
    }

    public static void shootJavelin(Player player, ToolStack tool){
        var direction = player.getLookAngle();
        var horizontal = direction.cross(new Vec3(0.01,1,0));
        var proj = new MeleeFieryJavelinProjectile(CtiEntity.MELEE_FIERY_JAVELIN.get(),player.getLevel());
        proj.tool = tool;
        proj.setPos(player.getBoundingBox().getCenter());
        proj.shoot(direction.x,direction.y,direction.z,0.75f,0);
        proj.setOwner(player);
        player.level.addFreshEntity(proj);
        if (player.hasEffect(CtiEffects.OVERHEAT.get())){
            var scatterDirection = direction.add(horizontal.scale(0.151));
            proj = new MeleeFieryJavelinProjectile(CtiEntity.MELEE_FIERY_JAVELIN.get(),player.getLevel());
            proj.tool = tool;
            proj.setPos(player.getBoundingBox().getCenter());
            proj.shoot(scatterDirection.x,scatterDirection.y,scatterDirection.z,0.4f+0.2f*RANDOM.nextFloat(),0);
            proj.setOwner(player);
            player.level.addFreshEntity(proj);

            scatterDirection = direction.add(horizontal.scale(-0.151));
            proj = new MeleeFieryJavelinProjectile(CtiEntity.MELEE_FIERY_JAVELIN.get(),player.getLevel());
            proj.tool = tool;
            proj.setPos(player.getBoundingBox().getCenter());
            proj.shoot(scatterDirection.x,scatterDirection.y,scatterDirection.z,0.4f+0.2f*RANDOM.nextFloat(),0);
            proj.setOwner(player);
            player.level.addFreshEntity(proj);
        }
        player.level.playSound(null,player, SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS,1,1.5f);
    }
}
