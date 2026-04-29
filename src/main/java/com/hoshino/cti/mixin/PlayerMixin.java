package com.hoshino.cti.mixin;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import static com.hoshino.cti.util.method.GetModifierLevel.EquipHasModifierlevel;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getAttackStrengthScale", at = @At(value = "RETURN"), cancellable = true)
    public void changeScale(float p_36404_, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        float delay = player.getCurrentItemAttackStrengthDelay();
        if (delay < 10) {
            cir.setReturnValue(Mth.clamp(((float) ((LivingEntityAccessor) player).getAttackStrengthTicker() + p_36404_) / (delay * 0.7f + 3f), 0.0F, 1.0F));
        }
    }

    @Override
    public boolean fireImmune() {
        var modifierID=new ModifierId(Cti.getResource("drflamez"));
        Player player = (Player) (Object) this;
        if(GetModifierLevel.getTotalArmorModifierlevel(player,modifierID)>0){
            return true;
        }
        return super.fireImmune();
    }
}
