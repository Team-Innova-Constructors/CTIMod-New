package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.hooks.AfterPlasmaSlashHitModifierHook;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class AtomicPlasmaSlash extends NoLevelsModifier implements AfterPlasmaSlashHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, etshtinkerHook.AFTER_SLASH_HIT);
    }

    @Override
    public void afterPlasmaSlashHit(ToolStack tool, LivingEntity target, PlasmaSlashEntity slash, boolean isCritical, float slashDamage) {
        if (slash.getOwner() instanceof Player player)
            EntityTickerManager.getInstancePlayerSpecific(target,player.getUUID()).addTicker(
                    new EntityTickerInstance(CtiEntityTickers.ATOMIC_DECOMPOSE.get(), isCritical?1:2,200),Integer::sum,Integer::max
            );
    }
}
