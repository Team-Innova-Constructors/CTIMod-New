package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.defense;


import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;

@Mod.EventBusSubscriber
public class EnderSuppress extends EtSTBaseModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_ENDER_SUPPRESS = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("ender_suppress"));

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_ENDER_SUPPRESS,false, TinkerTags.Items.SHIELDS));
    }

    public static final String KEY_CD = "tiac_ender_supress_cd";

    @SubscribeEvent
    public static void stopEnderTeleport(EntityTeleportEvent event){
        if (event.getEntity() instanceof Mob mob&&mob.getTarget()!=null) {
            var manager = EntityTickerManager.getInstance(mob);
            if (manager.hasTicker(CtiEntityTickers.ENDER_STICTION.get())) {
                event.setCanceled(true);
                var entity = mob.getTarget();
                entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                    var lvl = cap.get(KEY_ENDER_SUPPRESS, 0);
                    if (lvl > 0) {
                        if (entity.getPersistentData().getInt(KEY_CD) > 0) return;
                        if (entity instanceof Player player && entity.getMainHandItem().is(TinkerTags.Items.MELEE_PRIMARY)) {
                            ToolAttackUtil.attackEntity(entity.getMainHandItem(), player, mob);
                            player.getPersistentData().putInt(KEY_CD, Math.max(1, (int) (player.getCurrentItemAttackStrengthDelay() * 0.5f)));
                        }
                    }
                });
            }
        }
    }
    @SubscribeEvent
    public static void onLivingTick(TickEvent.PlayerTickEvent event){
        var cd = event.player.getPersistentData().getInt(KEY_CD);
        if (cd>0)
            event.player.getPersistentData().putInt(KEY_CD,cd-1);
    }
}
