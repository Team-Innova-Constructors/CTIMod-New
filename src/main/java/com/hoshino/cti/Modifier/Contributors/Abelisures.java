package com.hoshino.cti.Modifier.Contributors;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.hoshino.cti.register.CtiEffects;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public class Abelisures extends ArmorModifier {
    {
        MinecraftForge.EVENT_BUS.addListener(this::WhenEffectExpire);
        MinecraftForge.EVENT_BUS.addListener(this::DeathEvent);
    }

    private void DeathEvent(LivingDeathEvent event) {
        if (event.getEntity().hasEffect(CtiEffects.Abel.get()) && event.getEntity() instanceof Player player) {
            player.setHealth(1);
            event.setCanceled(true);
        }
    }

    public @NotNull InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand interactionHand, InteractionSource interactionSource) {
        if (interactionSource == InteractionSource.RIGHT_CLICK) {
            GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, interactionHand);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    private void WhenEffectExpire(MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() == CtiEffects.Abel.get() && event.getEntity() instanceof ServerPlayer player) {
            Collection<MobEffectInstance> Effect = player.getActiveEffects();
            for (int i = 0; i < Effect.size(); i++) {
                MobEffectInstance effect = Effect.stream().toList().get(i);
                MobEffect harm = effect.getEffect();
                LivingEntity mob = player.getLastHurtMob();
                if (harm.getCategory() == MobEffectCategory.HARMFUL) {
                    if (mob != null) {
                        SuperpositionHandler.backToSpawn(player);
                        player.removeEffect(harm);
                        EffectUtil.refreshEffect(mob, new MobEffectInstance(harm, 400, 0), EffectUtil.AddReason.FORCE, mob);
                    }
                }
            }
        }
    }

    @Override
    public void MobEffectEvent(MobEffectEvent.Applicable event) {
        if (event.getEntity().getLastHurtByMob() != null) {
            if (event.getEntity().getLastHurtByMob() instanceof Player player) {
                if (player.hasEffect(CtiEffects.ev.get())) {
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }

    @Override
    public void onFinishUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity) {
        if (tool.getCurrentDurability() > 500 && entity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(CtiEffects.Abel.get(), 200 * modifier.getLevel(), 0));
            player.getCooldowns().addCooldown(tool.getItem(), 2400);
            ToolDamageUtil.damageAnimated(tool, 500, entity);
        }
    }

    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 10;
    }
}
