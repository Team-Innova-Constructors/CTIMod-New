package com.hoshino.cti.mixin;

import com.hoshino.cti.util.ChangeBossHealth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements ChangeBossHealth {
    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Unique
    @Override
    public void cti$changeMaxHealthAttributeInstance(Consumer<AttributeInstance> multimapConsumer) {
        var maxHealth = this.getAttributes().getInstance(Attributes.MAX_HEALTH);
        var armor = this.getAttributes().getInstance(Attributes.ARMOR);
        multimapConsumer.accept(maxHealth);
        multimapConsumer.accept(armor);
    }
}
