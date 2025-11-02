package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ReflectTrait.class, remap = false)
public class ReflectTraitMixin {
    /**
     * @author firefly
     * @reason 最新更改:
     * <br><h5>I.取消掉了莱特兰本身等级增幅对于反射伤害的增幅
     * <br>II.视为魔法伤害
     * <br>III.不会超过怪物最大生命 x 倍率</h5>
     * <br>监听器mixin在这边{@link AttackListenerMixin#onHurt(AttackCache, ItemStack, CallbackInfo)}
     */
    @Overwrite
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().isBypassInvul()) return;
        if(event.getSource().getEntity() instanceof LivingEntity lv&&event.getSource() instanceof EntityDamageSource source&&!source.isThorns()){
            if (CurioCompat.hasItem(lv, LHItems.ABRAHADABRA.get())) {
                return;
            }
            if(lv instanceof Player player&& GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
                return;
            }
            float Magnification = level * 0.3F;
            float reflectAmount =Math.min(event.getEntity().getHealth() * Magnification,event.getAmount() *0.1f*level);
            EntityDamageSource mobAttackReflect=new EntityDamageSource("mobattackreflect",entity).setThorns();
            mobAttackReflect.setScalesWithDifficulty().setMagic();
            lv.hurt(mobAttackReflect,reflectAmount);
        }
    }
}
