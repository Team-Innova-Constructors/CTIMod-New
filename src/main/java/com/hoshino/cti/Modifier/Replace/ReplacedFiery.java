package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.Modifier.genre.elemental.fiery.BasicBurntModifier;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.materialGenre.GenreManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.concurrent.atomic.AtomicInteger;

public class ReplacedFiery extends BasicBurntModifier {
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 20*modifierEntry.getLevel();
    }

    @Override
    public int getReleasedBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 5*modifierEntry.getLevel();
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()&&context.getTarget() instanceof LivingEntity living&&context.getAttacker() instanceof Player player){
            AtomicInteger elementalReleaseLv = new AtomicInteger(0);
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
            living.invulnerableTime = 0;
            living.hurt(ElementalDamageSource.fiery(context.getAttacker()),damage* GenreManager.calculateElementalRelease(elementalReleaseLv.get(),0.1f,0.2f) *modifier.getLevel());
        }
    }
}
