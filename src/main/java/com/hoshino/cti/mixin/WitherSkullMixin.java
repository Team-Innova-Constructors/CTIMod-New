package com.hoshino.cti.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
/**
 * @author <h1><li>firefly</li></h1>
 * @reason <h4>原本凋灵骷髅头堆积不会自然消失导致卡服务器</h4>
 */
@Mixin(WitherSkull.class)
public abstract class WitherSkullMixin extends AbstractHurtingProjectile {
    protected WitherSkullMixin(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    @Override
    public void tick() {
        if(this.tickCount>120){
            this.discard();
        }
        super.tick();
    }
}
