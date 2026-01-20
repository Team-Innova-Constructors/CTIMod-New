package com.hoshino.cti.mixin;

import com.hoshino.cti.util.ChangeBossHealth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements ChangeBossHealth {
    @Shadow @Nullable public abstract LivingEntity getTarget();

    @Shadow public abstract void setTarget(@org.jetbrains.annotations.Nullable LivingEntity p_21544_);

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
    @Inject(method = "tick",at = @At("TAIL"))
    private void tick(CallbackInfo ci){
        var target=this.getTarget();
        var data=this.getPersistentData();
        boolean teamed=data.getBoolean("player_teamed");
        if(teamed){
            if(target instanceof Player){
                this.setTarget(null);
            }
            if(this.getTarget() == null){
                var mobList = this.getLevel().getEntitiesOfClass(Monster.class, new AABB(this.getOnPos()).inflate(6));

                for(Mob mob : mobList){
                    if(mob.getPersistentData().getBoolean("player_teamed") || mob.getUUID() == this.getUUID()) continue;
                    this.setTarget(mob);
                    break;
                }
            }
        }
    }
}
