package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.IntSupplier;

@Mixin(value = RegenTrait.class, remap = false)
public abstract class RegenTaintMixin extends MobTrait {
    public RegenTaintMixin(IntSupplier color) {
        super(color);
    }

    /**
     * @author firefly
     * @reason 最新更改:
     * <br><h5>I.大幅度降低了基础恢复(每秒1.5%x等级->0.5%x等级)
     * <br>II.现在首次进入半血会附带黎明效果,快速恢复一部分生命,只触发一次
     * <br>III.触发黎明后怪物造成的伤害提升30%</h5>
     */
    @Overwrite
    public void tick(LivingEntity mob, int level) {
        if (!mob.getLevel().isClientSide()) {
            if (mob.tickCount % 20 == 0) {
                if(mob instanceof Mob mob1){
                    if(mob1.getTarget() instanceof Player player){
                        if(GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
                            return;
                        }
                    }
                }
                var data = mob.getPersistentData();
                boolean shouldAdd = !data.contains("cti_liming");
                if (mob.getHealth() <= mob.getMaxHealth() * 0.5f && shouldAdd) {
                    mob.getLevel().playSound(null, mob.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 1, 0.5f);
                    EntityTickerManager.getInstance(mob).addTicker(new EntityTickerInstance(CtiEntityTickers.DAWN.get(),1,60),Integer::max,Integer::max);
                    mob.getPersistentData().putInt("cti_liming", 1);
                }
                float healAmount = 0;
                healAmount += 0.005f * mob.getMaxHealth() * level;
                mob.heal(healAmount);
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean validTarget(LivingEntity le) {
        return !(le instanceof EnderDragon);
    }
}
