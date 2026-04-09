package com.hoshino.cti.integration.botania.modifiers.specialized;

import cofh.core.client.particle.options.CylindricalParticleOptions;
import cofh.core.init.CoreParticles;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.BurstHitModifierHook;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.entity.ThunderBurstEntity;
import com.hoshino.cti.integration.botania.modifiers.base.SpecializedBurstModifier;
import com.hoshino.cti.integration.botania.tool.DummyToolManaLens;
import com.hoshino.cti.util.ParticleContext;
import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;

import java.util.List;

import static com.hoshino.cti.integration.botania.modifiers.FartherSights.KEY_TRIGGER_TOOL;

public class Thunderstorm extends SpecializedBurstModifier implements BurstHitModifierHook, ModifyBurstModifierHook{
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiBotModifierHooks.MODIFY_BURST,CtiBotModifierHooks.BURST_HIT);
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burstExtras.addEntityPerConsumption(50);
        burstExtras.addBaseDamage(16);
        burst.setMana(burst.getMana()+200);
        burst.setColor(0xABFFE8);
    }


    @Override
    public void afterBurstHitEntity(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst,IManaBurstExtra burstExtra, float damage) {
        if (!(owner instanceof Player player))return;
        if (burstExtra.cti$getGeneration()>0) return;
        if (burst.getMana()<burstExtra.cti$getPerConsumption()*2) return;
        if (owner.level instanceof ServerLevel serverLevel) ParticleContext
                .buildParticle(new CylindricalParticleOptions(CoreParticles.SHOCKWAVE.get(),
                        5,5,0,0xAEFFFAFF,2))
                .setVelocity(0,0,0).setPos(target.getBoundingBox().getCenter()).build().sendToClient(serverLevel);
        burst.entity().playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE);
        BurstProperties properties = new BurstProperties(burst.getStartingMana(),burst.getMinManaLoss(),
                burst.getManaLossPerTick(),burst.getBurstGravity(),1,0xFFFFFF);
        int i = 0;
        do {
            ThunderBurstEntity entity = new ThunderBurstEntity(player);
            entity.setMana(burstExtra.cti$getPerConsumption());
            entity.setStartingMana(entity.getMana());
            entity.setGravity(properties.gravity);
            entity.setManaLossPerTick(properties.manaLossPerTick);
            entity.setMinManaLoss(properties.ticksBeforeManaLoss);
            entity.setPos(target.position().add(RANDOM.nextFloat()*8-4,9,RANDOM.nextFloat()*8-4));
            entity.setDeltaMovement(0,-3,0);
            ItemStack dummyLens = DummyToolManaLens.getDummyLens((ToolStack) tool);
            entity.setSourceLens(dummyLens);
            entity.cti$setTool(tool);
            if (burst.entity().getTags().contains(KEY_TRIGGER_TOOL)){
                entity.addTag(KEY_TRIGGER_TOOL);
            }
            var extras = (IManaBurstExtra) entity;
            extras.cti$setBaseDamage(burstExtra.cti$getBaseDamage());
            extras.cti$setGeneration(1);
            extras.cti$setPerConsumption(50);
            extras.cti$setPerBlockConsumption(0);
            extras.cti$setDamageModifier(0.25f);
            owner.level.addFreshEntity(entity);
            burst.setMana(burst.getMana()-burstExtra.cti$getPerConsumption());
            i++;
        } while (burst.getMana()>=burstExtra.cti$getPerConsumption()&&i<8);
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return DynamicComponentUtil.scrollColorfulText.getColorfulText(getTranslationKey(),null,
                new int[]{0x85C2FF,0x85C2FF,0xD5FFFE},30,10,true);
    }

}
