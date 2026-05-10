package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Cti;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

@Mod.EventBusSubscriber
public class FixedCelestial extends Modifier {
    public static TinkerDataCapability.TinkerDataKey<Integer> KEY = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("celestial"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY,false, null));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        var player = event.player;
        if (player.getAbilities().flying)
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
                int lvl = cap.get(KEY,0);
                if (lvl>0){
                    if (player.zza != 0f)
                        player.moveRelative(0.05f*lvl* Math.signum(player.zza), new Vec3(0, 0, 1));
                    if (player.xxa != 0f)
                        player.moveRelative(0.05f*lvl * Math.signum(player.xxa), new Vec3(1, 0, 0));
                    if (player.jumping&&!player.isShiftKeyDown())
                        player.moveRelative(0.05f*lvl, new Vec3(0, 1, 0));
                    if (player.isShiftKeyDown()&&!player.jumping)
                        player.moveRelative(0.05f*lvl, new Vec3(0, -1, 0));
                }
            });
    }
}
