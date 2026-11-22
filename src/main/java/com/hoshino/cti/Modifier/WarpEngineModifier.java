package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.c2h6s.etshtinker.tools.item.tinker.ConstrainedPlasmaSaber;
import com.hoshino.cti.Entity.Projectiles.PlasmaCircleSlashEntity;
import com.hoshino.cti.client.renderer.projectile.PlasmaCircleSlashRenderer;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PStopWarpEngineC2S;
import com.hoshino.cti.netwrok.packet.PTurnPlayerBackS2C;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static com.c2h6s.etshtinker.tools.item.tinker.ConstrainedPlasmaSaber.*;
import static com.c2h6s.etshtinker.util.SlashColor.getSlash;
import static slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper.TANK_HELPER;

@Mod.EventBusSubscriber
public class WarpEngineModifier extends Modifier implements GeneralInteractionModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        if (source==InteractionSource.RIGHT_CLICK){
            GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.BOW;
    }

    @Override
    public void onStoppedUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
        if (entity instanceof ServerPlayer player) {
            var instance = EntityTickerManager.getInstance(player);
            if (!instance.hasTicker(CtiEntityTickers.WARP_ENGINE.get()))
                instance.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.WARP_ENGINE.get(), modifier.getLevel(),1));
        }
    }

    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 72000;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event){
        if (event.getSide()== LogicalSide.CLIENT)
            CtiPacketHandler.sendToServer(new PStopWarpEngineC2S());
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            var instance = EntityTickerManager.getInstance(serverPlayer);
            if (instance.hasTicker(CtiEntityTickers.WARP_ENGINE.get()))
                instance.removeTicker(CtiEntityTickers.WARP_ENGINE.get());
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemUse(LivingEntityUseItemEvent.Start event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            var instance = EntityTickerManager.getInstance(serverPlayer);
            if (instance.hasTicker(CtiEntityTickers.WARP_ENGINE.get()))
                instance.removeTicker(CtiEntityTickers.WARP_ENGINE.get());
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLeftClickEntity(AttackEntityEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            var instance = EntityTickerManager.getInstance(serverPlayer);
            if (instance.hasTicker(CtiEntityTickers.WARP_ENGINE.get())) {
                instance.removeTicker(CtiEntityTickers.WARP_ENGINE.get());
                if (serverPlayer.getAttackStrengthScale(0)>0.8&&event.getTarget() instanceof LivingEntity living){
                    var itemStack = serverPlayer.getMainHandItem();
                    if (itemStack.getItem() instanceof ConstrainedPlasmaSaber){
                        ToolStack tool = ToolStack.from(itemStack);
                        if (tool.getModifierLevel(CtiModifiers.WARP_ENGINE_STRIKE.getId())>0){
                            createCircleSlash(serverPlayer,tool,living);
                            if (tool.getModifierLevel(CtiModifiers.WARP_ENGINE_TURNBACK.getId())>0)
                                CtiPacketHandler.sendToPlayer(new PTurnPlayerBackS2C(),serverPlayer);
                        }
                    }
                }
            }
        }
    }

    public static void createCircleSlash(ServerPlayer player, ToolStack tool,LivingEntity living){
        if (!(player.getMainHandItem().getItem() instanceof ConstrainedPlasmaSaber)){
            return;
        }
        if (!checkFluid(tool)||tool.isBroken()){
            return;
        }
        FluidStack fluidStack = TANK_HELPER.getFluid(tool);
        MeltingFuel fuel = getFuel(fluidStack.getFluid());
        int consumption =getFuelCumsp(fuel, fluidStack.getFluid(), tool);
        for (ModifierEntry modifier : tool.getModifierList()) {
            consumption = modifier.getHook(etshtinkerHook.FLUID_CONSUMPTION).getFluidConsumption(tool, fluidStack, player, consumption, consumption);
        }
        if (fluidStack.getAmount()< consumption){
            return;
        }
        float damage = getFuelDamage(fuel)*getDamageMultiplier(tool);
        int color = getSlash(tool.getStats().getInt(etshtinkerToolStats.SLASH_COLOR));
        Level level =player.getLevel();
        PlasmaSlashEntity slash =new PlasmaCircleSlashEntity(level,color);
        float scale = tool.getModifierLevel(ModifierIds.reach)+3;
        slash.damage=damage;
        slash.setOwner(player);
        slash.setDeltaMovement(player.getLookAngle().cross(player.getLookAngle().cross(new Vec3(0.01,1,0.01))).scale(RANDOM.nextBoolean()?1:-1));
        slash.setScale(scale);
        slash.setToolstack(tool);
        slash.fuelUsed = fuel;
        slash.fluidStack = fluidStack;
        slash.CriticalRate=tool.getStats().get(etshtinkerToolStats.CRITICAL_RATE);
        slash.setPos(living.getX(),living.getY()+0.5*living.getBbHeight(),living.getZ());
        for (ModifierEntry modifier : tool.getModifierList()) {
            slash = modifier.getHook(etshtinkerHook.SLASH_CREATE).plasmaSlashCreate(tool, fluidStack, player, slash);
        }
        level.addFreshEntity(slash);
        fluidStack.shrink(consumption);
        TANK_HELPER.setFluid(tool,fluidStack);
        ToolDamageUtil.damageAnimated(tool,1,player, InteractionHand.MAIN_HAND);
    }
}
