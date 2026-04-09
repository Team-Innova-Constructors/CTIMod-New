package com.hoshino.cti.Event;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.Projectiles.MeteorEntity;
import com.hoshino.cti.Event.ModEvents.MeteorSpawnEvent;
import com.hoshino.cti.Items.SlimeCanItem;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.DimensionConstants;
import com.xiaoyue.tinkers_ingenuity.content.basic.entity.projectile.ShurikenEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thelm.packagedauto.block.entity.CrafterBlockEntity;
import thelm.packagedauto.block.entity.PackagerBlockEntity;
import thelm.packagedauto.block.entity.PackagerExtensionBlockEntity;
import thelm.packagedauto.block.entity.UnpackagerBlockEntity;
import thelm.packagedavaritia.block.entity.ExtremeCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.AdvancedCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.EliteCrafterBlockEntity;
import thelm.packagedexcrafting.block.entity.UltimateCrafterBlockEntity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class ServerEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLightningStrikeEntity(EntityStruckByLightningEvent event){
        LightningBolt bolt = event.getLightning();
        if (bolt.getCause()!=null){
            if (event.getEntity() instanceof ItemEntity||event.getEntity() instanceof ExperienceOrb) event.setCanceled(true);
            else if (event.getEntity()==bolt.getCause()) event.setCanceled(true);
            else if (bolt.getTags().contains("valkyrie")){
                DamageSource source = new EntityDamageSource(DamageSource.LIGHTNING_BOLT.msgId,bolt.getCause()).bypassArmor();
                event.getEntity().invulnerableTime=0;
                event.getEntity().hurt(source,bolt.getDamage());
                event.getEntity().invulnerableTime=0;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event){
        EntityTickerManager.saveAll();
    }
    @SubscribeEvent
    public static void onEntityTravelDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof ShurikenEntity entity) {
            entity.discard();
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof FallingBlockEntity) event.setCanceled(true);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.getLevel() instanceof ServerLevel level ) {
            Player player = event.player;
            if (level.dimension().equals(DimensionConstants.MOON) && level.getGameTime() % 2000 == 0)
                if (Math.abs(player.getX()) + Math.abs(player.getZ()) > 750) {
                    Random random = new Random();
                    if (random.nextInt(10) == 0) {
                        Vec2 pos = new Vec2((float) (player.getX() + random.nextFloat() * 192), (float) (player.getZ() + random.nextFloat() * 192));
                        if (level.hasChunkAt(new BlockPos(pos.x, 350, pos.y))) {
                            MeteorSpawnEvent event1 = new MeteorSpawnEvent(new Vec3(pos.x, 350, pos.y));
                            MinecraftForge.EVENT_BUS.post(event1);
                            if (!event1.isCanceled()) {
                                MeteorEntity entity = new MeteorEntity(level, pos.x, 350, pos.y, new Vec3(0, random.nextFloat() * 2.5 - 1.5, 0));
                                entity.setExplosionPower((byte) (random.nextInt(125) + 25));
                                level.addFreshEntity(entity);
                            }
                        }
                    }
                }
//            CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler ->
//                    handler.getStacksHandler("slime_can").ifPresent(stackHandler -> {
//                        for (int i = 0; i < stackHandler.getStacks().getSlots(); i++) {
//                            var stack = stackHandler.getStacks().getStackInSlot(i);
//                            if (!stack.isEmpty() && stack.getItem() instanceof SlimeCanItem)
//                                SlimeCanItem.tick(new SlotContext("slime_can", player, i, stackHandler.hasCosmetic(), stackHandler.isVisible()), stack);
//                        }
//                    }));
        }
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event){
        tweakPackagedAuto();
    }

    //加速封包合成
    public static void tweakPackagedAuto(){
        CrafterBlockEntity.energyReq=500;
        CrafterBlockEntity.energyUsage=500;
        UltimateCrafterBlockEntity.energyReq=500;
        UltimateCrafterBlockEntity.energyUsage =500;
        EliteCrafterBlockEntity.energyReq=500;
        EliteCrafterBlockEntity.energyUsage=500;
        AdvancedCrafterBlockEntity.energyReq=500;
        AdvancedCrafterBlockEntity.energyUsage=500;
        ExtremeCrafterBlockEntity.energyReq=500;
        ExtremeCrafterBlockEntity.energyUsage=500;
        PackagerBlockEntity.energyReq = 500;
        PackagerBlockEntity.energyUsage = 250;
        PackagerExtensionBlockEntity.energyReq=500;
        PackagerExtensionBlockEntity.energyUsage=250;
    }
}
