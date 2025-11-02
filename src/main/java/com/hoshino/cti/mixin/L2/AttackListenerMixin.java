package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.events.LHAttackListener;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import dev.xkmc.l2library.init.events.attack.AttackListener;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/**
 * @author firefly
 *<h5>反射伤害太高,改为不能被莱特兰增伤自我增幅</h5>
 */
@Mixin(value = LHAttackListener.class,remap = false)
public class AttackListenerMixin implements AttackListener {
    @Inject(at = {@At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/backport/damage/DamageModifier;hurtMultTotal(Ldev/xkmc/l2library/init/events/attack/AttackCache;F)V")}, method = {"onHurt"}, cancellable = true)
    public void onHurt(AttackCache cache, ItemStack weapon, CallbackInfo ci){
        if(cache.getLivingHurtEvent()!=null&&cache.getLivingHurtEvent().getSource()!=null){
            var damagesource=cache.getLivingHurtEvent().getSource();
            if(damagesource.getMsgId().contains("mobattackreflect")){
                ci.cancel();
            }
        }
    }
}
