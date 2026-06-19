package com.hoshino.cti.mixin.Aquamixin;

import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.L2.KnlyHelper;
import com.obscuria.aquamirae.common.entities.CaptainCornelia;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = CaptainCornelia.class, remap = false)
public abstract class CaptainCorneliaMixin extends Monster {
    protected CaptainCorneliaMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Override
    protected boolean canRide(Entity pVehicle) {
        return false;
    }
    @Inject(method = "switchWeapon",at = @At("HEAD"), cancellable = true)
    private void switchW(CallbackInfo ci){
        if(getMainHandItem().isEmpty())return;
        ci.cancel();
    }
    @Unique
    private static EntityDamageSource cti_new$KNLY(LivingEntity entity){
        var s=new EntityDamageSource("keniliyahurt",entity);
        s.bypassArmor().bypassMagic();
        return s;
    }
    @Inject(method = "m_7327_",at = @At("HEAD"))
    private void attack(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(entity instanceof LivingEntity living){
            var stack=getMainHandItem();
            if(stack.is(TinkerTags.Items.MODIFIABLE)){
                var tool= ToolStack.from(stack);
                if(tool.getModifierLevel(CtiModifiers.BURIED_OCEAN_STATIC_MODIFIER.getId())>0){
                    KnlyHelper.checkAndGiveData(living);
                    if(living instanceof Mob mob){
                        var mobLevel = DifficultyLevel.ofAny(this);
                        mob.hurt(cti_new$KNLY(this),mobLevel * 0.5f);
                    }
                    else if(living instanceof Player player){
                        var mobLevel = DifficultyLevel.ofAny(this);
                        player.hurt(cti_new$KNLY(this),mobLevel * 0.1f);
                    }
                }
            }
        }
    }
}
