package com.hoshino.cti.Entity.Projectiles;

import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class GhostSwordProjectile extends EntityGhostSword {
    public GhostSwordProjectile(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
    }
}
