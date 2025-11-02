package com.hoshino.cti.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import quek.undergarden.entity.boss.Masticator;

@Mixin(Masticator.class)
public abstract class MasticatorMixin extends Monster {

    protected MasticatorMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        var currentTarget=this.getTarget();
        if(pSource.getEntity() instanceof LivingEntity attacker){
            if(currentTarget instanceof Player){
                if(attacker instanceof Player player1){
                    this.setTarget(player1);
                }
            }
            else this.setTarget(attacker);
        }
        return super.hurt(pSource,pAmount);
    }
}
