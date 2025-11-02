package com.hoshino.cti.Modifier.Contributors;

import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class LebegusProtection extends ArmorModifier {
    private static final TinkerDataCapability.TinkerDataKey<Integer> key = TConstruct.createKey("lebegusprotection");

    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(key, false, null));
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                int level = holder.get(key, 0);
                event.setAmount(event.getAmount() * 1 - (0.06F * level));
            });
        }
    }
}
