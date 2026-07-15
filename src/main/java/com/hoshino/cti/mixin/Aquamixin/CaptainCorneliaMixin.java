package com.hoshino.cti.mixin.Aquamixin;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
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
import net.minecraft.world.item.ItemStack;
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

    @Inject(method = "switchWeapon", at = @At("HEAD"), cancellable = true)
    private void switchW(CallbackInfo ci) {
        if (getMainHandItem().isEmpty()) return;
        ci.cancel();
    }

    @Unique
    private static EntityDamageSource cti_new$KNLY(LivingEntity entity) {
        var s = new EntityDamageSource("keniliyahurt", entity);
        s.bypassArmor().bypassMagic().bypassEnchantments().bypassInvul();
        return s;
    }

    @Inject(method = "m_7327_", at = @At("HEAD"))
    private void attack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof LivingEntity living)) return;
        ItemStack stack = getMainHandItem();
        boolean hasKnife = stack.is(TinkerTags.Items.MODIFIABLE) && ToolStack.from(stack).getModifierLevel(CtiModifiers.BURIED_OCEAN_STATIC_MODIFIER.getId()) > 0;
        boolean shouldGiveData = hasKnife || this.random.nextBoolean();
        if (shouldGiveData) {
            KnlyHelper.checkAndGiveData(living);
        }
        float mobLevel = DifficultyLevel.ofAny(this);
        float damageMultiplier = living instanceof Mob
                ? (hasKnife ? 1.0f : 0.25f)
                : (hasKnife ? 0.2f : 0.1f);
        var manager = EntityTickerManager.getInstance(living);
        manager.addTicker(new EntityTickerInstance(CtiEntityTickers.NIGHTMARE.get(), 1,100),Integer::max,Integer::max);
        manager.addTicker(new EntityTickerInstance(CtiEntityTickers.STRICT_CURSE.get(), 1,200),Integer::max,Integer::max);
        living.hurt(cti_new$KNLY(this), mobLevel * damageMultiplier);
    }
}
