package com.hoshino.cti.Modifier.underGardenCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.hoshino.cti.Cti;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import quek.undergarden.entity.stoneborn.Stoneborn;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class RegaliumModifier extends EtSTBaseModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_REGALIUM = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("key_regallium"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(KEY_REGALIUM,false, TinkerTags.Items.MODIFIABLE));
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event){
        if (event.getEntity() instanceof Stoneborn stoneborn&&event.getNewTarget()!=null) {
            event.getNewTarget().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                if (cap.get(KEY_REGALIUM, 0) > 0&&stoneborn.getLastHurtByMob()!=event.getNewTarget()) {
                    event.setCanceled(true);
                }
            });
        }
    }

    @Override
    public void modifierAddToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        List.of(
                ToolStats.ATTACK_DAMAGE,
                ToolStats.PROJECTILE_DAMAGE,
                ToolStats.ATTACK_SPEED,
                ToolStats.DRAW_SPEED,
                ToolStats.ARMOR,
                ToolStats.ARMOR_TOUGHNESS,
                ToolStats.DURABILITY,
                ToolStats.MINING_SPEED,
                etshtinkerToolStats.PLASMARANGE,
                etshtinkerToolStats.SCALE,
                etshtinkerToolStats.ENERGY_STORE,
                etshtinkerToolStats.CRITICAL_RATE,
                etshtinkerToolStats.DAMAGEMULTIPLIER,
                etshtinkerToolStats.FLUID_EFFICIENCY
        ).forEach(stat->stat.percent(modifierStatsBuilder,0.20*modifierEntry.getLevel()));
    }
}
