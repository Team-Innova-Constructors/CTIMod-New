package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.concurrent.atomic.AtomicInteger;

public class StellarFiery extends BasicBurntModifier {
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 75*modifierEntry.getLevel();
    }

    @Override
    public int getReleasedBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 15*modifierEntry.getLevel();
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()&&context.getTarget() instanceof LivingEntity living&&context.getAttacker() instanceof Player player){
            AtomicInteger elementalReleaseLv = new AtomicInteger(0);
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
            EntityTickerInstance instance = EntityTickerManager.getInstance(player).getTicker(CtiEntityTickers.STELLAR_FIERY.get());
            float stackMultiplier = instance != null ? 1 + instance.level : 1f;
            living.invulnerableTime = 0;
            living.hurt(ElementalDamageSource.fiery(context.getAttacker()),damage* GenreManager.calculateElementalRelease(elementalReleaseLv.get(),0.25f,0.8f) * (player.hasEffect(CtiEffects.OVERHEAT.get())?0.5f:1) * modifier.getLevel()*stackMultiplier);
        }
    }
}
