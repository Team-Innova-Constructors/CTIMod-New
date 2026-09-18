package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.entityTicker.tickers.ReflectTicker;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.DelayDamageTickerRecord;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

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
        if (event.getSource().getEntity() instanceof LivingEntity lv && event.getSource() instanceof EntityDamageSource source && !source.isThorns() && lv != entity) {
            if (CurioCompat.hasItem(lv, LHItems.ABRAHADABRA.get())) {
                return;
            }
            if (lv instanceof Player player && GetModifierLevel.curioHasModifierLevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())) {
                return;
            }
            UUID suffer = entity.getUUID();
            UUID attackerUuid = lv.getUUID();
            float damageAmount = event.getAmount();
            if (!EntityTickerManager.getInstance(entity).hasTicker(CtiEntityTickers.REFLECT_TICKER.get())) {
                EntityTickerManager.getInstance(entity).addTicker(new EntityTickerInstance(CtiEntityTickers.REFLECT_TICKER.get(), 1, 20), Integer::max, Integer::max);
            }
            ReflectTicker.DAMAGE_MAP.compute(suffer, (uuid, record) -> {
                if (record == null || !record.attacker().equals(attackerUuid)) {
                    return new DelayDamageTickerRecord(attackerUuid, damageAmount, level);
                } else {
                    return record.addDamage(damageAmount);
                }
            });
        }
    }
}
