package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;

public class Dawn extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if(entity.tickCount%5==0){
            if(!(entity instanceof LivingEntity living))return true;
            if(living instanceof Player player){
                player.heal(player.getMaxHealth() * 0.066f);
            }
            else {
                var healAmount=living.getMaxHealth() * 0.08f;
                ForgeEventFactory.onLivingHeal(living, healAmount);
                living.setHealth(Math.min(living.getMaxHealth(),living.getHealth()+healAmount));
            }
        }
        return true;
    }
}
