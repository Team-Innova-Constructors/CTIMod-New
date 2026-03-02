package com.hoshino.cti.Entity.Projectiles.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
public class BasicElementalOrbEntity extends BasicFlyingSwordEntity {
    public BasicElementalOrbEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public int maxNodes = 6;

    @Override
    public void tick() {
        super.tick();
        getPositionCache().add(position());
        if (getPositionCache().size()>maxNodes) getPositionCache().remove(0);
    }
}
