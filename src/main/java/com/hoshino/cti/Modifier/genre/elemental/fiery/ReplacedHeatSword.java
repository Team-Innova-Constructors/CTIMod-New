package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.c2h6s.etshtinker.util.vecCalc;
import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.Projectiles.FieryCircleSlashEntity;
import com.hoshino.cti.Entity.Projectiles.FierySlashProjectile;
import com.hoshino.cti.Entity.Projectiles.RustedFireballEntity;
import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PClientSwingHandS2C;
import com.hoshino.cti.netwrok.packet.PSyncAttackTickerS2C;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class ReplacedHeatSword extends BasicOverHeatModifier implements GeneralInteractionModifierHook, AttributesModifierHook {
    public static final ResourceLocation KEY_RUST = Cti.getResource("heats_sword_rust");

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.ATTRIBUTES,ModifierHooks.GENERAL_INTERACT);
    }

    public static void shootSlash(IToolStackView tool, Player shooter, ModifierEntry modifier){
        var fierySlash = new FierySlashProjectile(CtiEntity.FIERY_SLASH.get(),shooter.getLevel());
        fierySlash.setDeltaMovement(shooter.getLookAngle().scale(7f));
        fierySlash.setOwner(shooter);
        fierySlash.tool = tool;
        fierySlash.burntLevel = modifier.getLevel();
        fierySlash.setPos(shooter.position().add(0,shooter.getBbHeight()/2,0));
        shooter.level.addFreshEntity(fierySlash);
        shooter.level.playSound(null,shooter, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,1,0.75f);
        shooter.level.playSound(null,shooter, BotaniaSounds.endoflame, SoundSource.PLAYERS,2,1.5f);
    }

    public static void increaseRust(IToolStackView tool,int amount){
        var toolData = tool.getPersistentData();
        toolData.putInt(KEY_RUST, Mth.clamp(toolData.getInt(KEY_RUST)+amount,0,100));
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getAttacker() instanceof Player player&&context.getTarget() instanceof LivingEntity target&&player.hasEffect(CtiEffects.OVERHEAT.get())){
            AtomicInteger elementalReleaseLv = new AtomicInteger(0);
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
            target.invulnerableTime = 0;
            target.hurt(ElementalDamageSource.burnt(player),damage* GenreManager.calculateElementalRelease(elementalReleaseLv.get(),0.05f,0.55f) *modifier.getLevel());
            target.invulnerableTime = 0;
        }
    }

    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 125;
    }

    @Override
    public int getReleasedBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 75;
    }

    @Override
    public float getBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 0.15f*modifierEntry.getLevel();
    }

    @Override
    public float getReleasedBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 0.25f*modifierEntry.getLevel();
    }


    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (tool.getPersistentData().contains(KEY_RUST, Tag.TAG_INT)){
            consumer.accept(Attributes.ATTACK_DAMAGE,new AttributeModifier(CommonUtil.UUIDFromSlot(slot,modifier.getId()),"desc.cti.heats_sword_rust",-0.005*tool.getPersistentData().getInt(KEY_RUST), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        if (tool.getPersistentData().contains(KEY_RUST,Tag.TAG_INT)&&!player.hasEffect(CtiEffects.OVERHEAT.get())&&source==InteractionSource.RIGHT_CLICK){
            GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUsingTick(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int maxCharge = (int) (60/getOverHeatAttackSpeed(player));
            if (72000-timeLeft==maxCharge)
                player.playSound(SoundEvents.GRINDSTONE_USE);
        }
    }

    @Override
    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.SPEAR;
    }

    @Override
    public void onStoppedUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int maxCharge = (int) (60/getOverHeatAttackSpeed(player));
            if (72000-timeLeft>=maxCharge){
                fieryCircleSlash(tool,modifier,player);
            }
        }
    }

    public static float getOverHeatAttackSpeed(Player player){
        AtomicInteger elementalReleaseLv = new AtomicInteger(0);
        player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
        return GenreManager.calculateElementalRelease(elementalReleaseLv.get(), (float) player.getAttributeValue(Attributes.ATTACK_SPEED), (float) player.getAttributeValue(CtiAttributes.BURNT_ATTACK_SPEED.get()));
    }

    public static void fieryCircleSlash(IToolStackView tool,ModifierEntry modifier,Player player){
        AtomicInteger elementalReleaseLv = new AtomicInteger(0);
        player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
        var fierySlash = new FieryCircleSlashEntity(CtiEntity.FIERY_CIRCLE_SLASH.get(),player.getLevel());
        int rust = tool.getPersistentData().getInt(KEY_RUST);
        tool.getPersistentData().remove(KEY_RUST);
        fierySlash.setDeltaMovement(player.getLookAngle());
        fierySlash.setOwner(player);
        fierySlash.tool = tool;
        fierySlash.burntLevel = modifier.getLevel();
        fierySlash.setPos(player.position().add(0,player.getBbHeight()/2,0));
        player.level.addFreshEntity(fierySlash);
        if (player instanceof ServerPlayer serverPlayer) {
            CtiPacketHandler.sendToPlayer(new PClientSwingHandS2C(InteractionHand.MAIN_HAND), serverPlayer);
            CtiPacketHandler.sendToPlayer(new PSyncAttackTickerS2C(0),serverPlayer);
            serverPlayer.resetAttackStrengthTicker();
        }
        if (rust>=12){
            for (int i =0;i<rust/12;i++){
                var fireBall = new RustedFireballEntity(player.getLevel());
                fireBall.setOwner(player);
                fireBall.damage = (float) (GenreManager.calculateElementalRelease(elementalReleaseLv.get(),1,0.2f)*player.getAttributeValue(CtiAttributes.BURNT_ATTACK_SPEED.get())*player.getAttributeValue(CtiAttributes.BURNT_INFLICT.get()));
                fireBall.setPos(player.position().add(0,player.getBbHeight()/2,0));
                fireBall.setDeltaMovement(vecCalc.getScatteredVec3(player.getLookAngle(),0.6).scale(2));
                player.level.addFreshEntity(fireBall);
            }
            player.level.playSound(null,player, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cataclysm:flame_burst")), SoundSource.PLAYERS,1,1.5f);
        }
        else player.level.playSound(null,player, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cataclysm:flame_burst")), SoundSource.PLAYERS,1,2);

    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide&&holder.hasEffect(CtiEffects.OVERHEAT.get())&& RANDOM.nextBoolean()){
            increaseRust(tool,1);
        }
    }

    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 72000;
    }
}
