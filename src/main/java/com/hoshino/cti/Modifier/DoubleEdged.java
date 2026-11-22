package com.hoshino.cti.Modifier;

import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PAttackSelfC2S;
import com.hoshino.cti.netwrok.packet.PFreezeAttackTickerS2C;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.CommonUtil;
import com.hoshino.cti.util.mixin.IPlayerMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

@Mod.EventBusSubscriber
public class DoubleEdged extends NoLevelsModifier implements MeleeDamageModifierHook,MeleeHitModifierHook {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerAttack(AttackEntityEvent event){
        var player = event.getEntity();
        var mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(mainHandItem);
            if (toolStack.getModifierLevel(CtiModifiers.double_edged.getId())>0){
                float charge = player.getAttackStrengthScale(0);
                if (charge>=0.6&&charge<0.75){
                    if (player instanceof ServerPlayer serverPlayer){
                        var managerInstance = EntityTickerManager.getInstance(player);
                        if (!managerInstance.hasTicker(CtiEntityTickers.DOUBLE_EDGED.get())) {
                            ToolAttackUtil.attackEntity(toolStack, player, InteractionHand.MAIN_HAND, event.getTarget(), () -> 1, false, EquipmentSlot.MAINHAND);
                            event.getTarget().invulnerableTime = 0;
                            managerInstance.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.DOUBLE_EDGED.get(), 1, 200));
                            managerInstance.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.DOUBLE_EDGED_RECOVERY.get(), player.attackStrengthTicker,0 ));
                            event.setCanceled(true);
                            serverPlayer.getLevel().sendParticles(CtiParticleType.WHITE_SPARK.get(),event.getTarget().getX(),event.getTarget().getY()+0.5*event.getTarget().getBbHeight(),event.getTarget().getZ(),1,0,0,0,1);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.MELEE_HIT);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() instanceof Player player&&context.isFullyCharged()){
            var instance = EntityTickerManager.getInstance(player);
            var tickerInstance = instance.getTicker(CtiEntityTickers.DOUBLE_EDGED.get());
            if (tickerInstance!=null){
                damage*=3f;
            }
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getAttacker() instanceof Player player) {
            var instance = EntityTickerManager.getInstance(player);
            var tickerInstance = instance.getTicker(CtiEntityTickers.DOUBLE_EDGED.get());
            if (tickerInstance!=null){
                instance.removeTicker(CtiEntityTickers.DOUBLE_EDGED.get());
            }
        }
    }
}
