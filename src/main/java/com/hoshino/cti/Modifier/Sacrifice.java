package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Sacrifice extends EtSTBaseModifier implements GeneralInteractionModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        GeneralInteractionModifierHook.startUsing(tool,modifier.getId(),player,hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 1;
    }

    @Override
    public void onFinishUsing(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull LivingEntity entity) {
        var tickerManager = EntityTickerManager.getInstance(entity);
        if (entity.isShiftKeyDown()){
            tickerManager.addTicker(new EntityTickerInstance(CtiEntityTickers.SACRIFICE_SEAL.get(),1,200),Integer::sum,Integer::max);
        }else {
            tickerManager.addTicker(new EntityTickerInstance(CtiEntityTickers.SACRIFICE_SEAL.get(),1,200),Integer::max,Integer::sum);
        }
        if (entity instanceof Player player) player.getCooldowns().addCooldown(player.getItemInHand(player.getUsedItemHand()).getItem(),8);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player!=null){
            var tickerInstance = EntityTickerManager.getInstance(player).getTicker(CtiEntityTickers.SACRIFICE_SEAL.get());
            if (tickerInstance!=null){
                tooltip.add(Component.translatable("cti.tooltip.modifier.sacrifice_level").append(String.valueOf(tickerInstance.level)).withStyle(ChatFormatting.RED));
                tooltip.add(Component.translatable("cti.tooltip.modifier.sacrifice_time").append(tickerInstance.duration / 20 +"s").withStyle(ChatFormatting.RED));
            }
        }
    }
}
