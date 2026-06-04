package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.L2.CorodiHelper;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.SlotIterateDamageTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SlotIterateDamageTrait.class,remap = false)
public abstract class SlotIterateDamageTraitMixin extends MobTrait {
    public SlotIterateDamageTraitMixin(ChatFormatting format) {
        super(format);
    }
    @Inject(method = "process", at = @At("HEAD"))
    private void onProcessEquipmentDamage(int level, LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Integer> cir) {
        if (attacker instanceof Mob mob && target instanceof Player player) {
            mob.getCapability(MobTraitCap.CAPABILITY).ifPresent(cap -> {
                int traitLevel = cap.getTraitLevel(LHTraits.CORROSION.get()) + cap.getTraitLevel(LHTraits.EROSION.get());
                if (traitLevel > 0) {
                    CorodiHelper.checkAndGiveData(player, traitLevel);
                }
            });
        }
    }
}
