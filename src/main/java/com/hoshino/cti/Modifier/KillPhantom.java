package com.hoshino.cti.Modifier;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class KillPhantom extends BattleModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(entity instanceof Player player&&isCorrectSlot){
            List<Phantom> phantoms = entity.level.getEntitiesOfClass(Phantom.class, new AABB(entity.getX() + 30, entity.getY() + 30, entity.getZ() + 30, entity.getX() -30, entity.getY() - 30, entity.getZ() - 30));
            for(Phantom phantom:phantoms){
                phantom.die(DamageSource.playerAttack(player));
                phantom.remove(Entity.RemovalReason.KILLED);
            }
        }
    }
}
