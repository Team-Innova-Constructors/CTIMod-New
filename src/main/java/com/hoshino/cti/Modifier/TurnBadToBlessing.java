package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ToolType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnBadToBlessing extends EtSTBaseModifier {
    public static final ResourceLocation KEY_TBTB = new ResourceLocation("tbtb_blessing");
    private static final Map<MobEffect,Float> SPECIAL_EFFECT_MAP = new HashMap<>();
    public static Map<MobEffect,Float> getSpecialEffectMap(){
        if (SPECIAL_EFFECT_MAP.isEmpty()){
            SPECIAL_EFFECT_MAP.put(CtiEffects.OVERHEAT.get(),20f);
            SPECIAL_EFFECT_MAP.put(CtiEffects.NARROW_ESCAPE.get(),100f);
            SPECIAL_EFFECT_MAP.put(TinkerModifiers.insatiableEffect.get(ToolType.MELEE), 1.25f);
            SPECIAL_EFFECT_MAP.put(TinkerModifiers.insatiableEffect.get(ToolType.RANGED), 1.25f);
            SPECIAL_EFFECT_MAP.put(TinkerModifiers.insatiableEffect.get(ToolType.ARMOR), 1.25f);
        }
        return SPECIAL_EFFECT_MAP;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() instanceof Player player&&context.isFullyCharged()){
            if (tool.getPersistentData().contains(KEY_TBTB, Tag.TAG_FLOAT)){
                damage+=damage*tool.getPersistentData().getFloat(KEY_TBTB)*(0.75f+0.25f*modifier.getLevel());
                tool.getPersistentData().remove(KEY_TBTB);
            }
        }
        return damage;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getAttacker() instanceof ServerPlayer player){
            float total = 0;
            for (var effectInstance: List.copyOf(player.getActiveEffects())){
                var effect = effectInstance.getEffect();
                float points = effectInstance.getDuration()*(1+effectInstance.getAmplifier());
                if (SPECIAL_EFFECT_MAP.containsKey(effect))
                    points*=SPECIAL_EFFECT_MAP.get(effect);
                points*=0.00001f;
                total+=points;
                ((LivingEntity)player).onEffectRemoved(effectInstance);
                player.activeEffects.remove(effect);
            }
            List.copyOf(EntityTickerManager.getInstance(player).getAllTickers()).forEach(entityTickerInstance -> {
                if (entityTickerInstance.ticker.category== MobEffectCategory.HARMFUL)
                    EntityTickerManager.getInstance(player).removeTicker(entityTickerInstance.ticker);
            });
            if (total>0)
                player.level.playSound(null,player, SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.PLAYERS,1,0.8f);
            tool.getPersistentData().putFloat(KEY_TBTB,total);
        }
    }
}
