package com.hoshino.cti.Modifier.genre.resourceConsuming.mana.forTrait;

import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.hooks.AfterPlasmaSlashHitModifierHook;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.NotEnoughManaPacket;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hoshino.cti.api.interfaces.IToolProvider;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.tool.DummyToolManaLens;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

import static com.hoshino.cti.Modifier.genre.resourceConsuming.mana.ManaResonance.KEY_MANA_RESONANCE;

//用ModifierTraitModule附加这个
public class LCManaBurstModifier extends Modifier implements LeftClickModifierHook, MeleeHitModifierHook, TooltipModifierHook, AfterPlasmaSlashHitModifierHook {
    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiModifierHook.LEFT_CLICK, ModifierHooks.MELEE_HIT,ModifierHooks.TOOLTIP, etshtinkerHook.AFTER_SLASH_HIT);
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player.getAttackStrengthScale(0)>0.9&&!level.isClientSide){
            var burst = getBurst(player, (ToolStack) tool);
            if (ManaItemHandler.instance().requestManaExactForTool(((ToolStack) tool).createStack(),player,burst.getMana(),true)
                    ||(tool.getVolatileData().getBoolean(KEY_MANA_RESONANCE)&&isSpellManaEnough(player,(int) Math.ceil(burst.getMana()*0.25f)))){
                launchBurst(tool,player,burst);
            }
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.9&&!level.isClientSide){
            var burst = getBurst(player, (ToolStack) tool);
            if (ManaItemHandler.instance().requestManaExactForTool(((ToolStack) tool).createStack(),player,burst.getMana(),true)
                    ||(tool.getVolatileData().getBoolean(KEY_MANA_RESONANCE)&&isSpellManaEnough(player,(int) Math.ceil(burst.getMana()*0.25f)))){
                launchBurst(tool,player,burst);
            }
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (tool.getModifierLevel(CtiModifiers.FAR_SIGHTS.get())<=0&&!context.isExtraAttack()&&context.isFullyCharged()&&context.getAttacker() instanceof Player player&&!(player instanceof FakePlayer)){
            var burst = getBurst(player, (ToolStack) tool);
            if (ManaItemHandler.instance().requestManaExactForTool(((ToolStack) tool).createStack(),player,burst.getMana(),true)
            ||(tool.getVolatileData().getBoolean(KEY_MANA_RESONANCE)&&isSpellManaEnough(player, (int) Math.ceil(burst.getMana()*0.25f)))){
                launchBurst(tool,player,burst);
            }
        }
        return knockback;
    }

    public static boolean isSpellManaEnough(Player player,int cost){
        var totalCost =cost - ManaUtil.getPlayerDiscounts(player,null);
        var manaCap = CapabilityRegistry.getMana(player).orElse(null);
        if (manaCap == null)
            return false;
        boolean canCast = totalCost <= manaCap.getCurrentMana() || player.isCreative();
        if (!player.level.isClientSide) {
            if (!canCast) {
                PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.spell.no_mana"));
                if (player instanceof ServerPlayer serverPlayer)
                    Networking.sendToPlayerClient(new NotEnoughManaPacket(totalCost), serverPlayer);
            }
            else manaCap.removeMana(totalCost);
        }
        return canCast;
    }

    public static void launchBurst(IToolStackView tool,Player player,ManaBurstEntity burst){
        tool.getModifierList().forEach(entry -> entry.getHook(CtiBotModifierHooks.MODIFY_BURST).burstLaunch(tool,entry,tool.getModifierList(),player, (ManaBurst) burst,(IManaBurstExtra) burst, (ToolStack) tool));
        player.level.addFreshEntity(burst);
        burst.playSound(BotaniaSounds.terraBlade);
    }

    public static ManaBurstEntity getBurst(Player player, ToolStack toolStack){
        ManaBurstEntity burst = new ManaBurstEntity(player);
        burst.setColor(2162464);
        burst.setMana(25);
        burst.setMinManaLoss(40);
        burst.setManaLossPerTick(2.0F);
        burst.setGravity(0.0F);
        burst.setDeltaMovement(burst.getDeltaMovement().scale(7));
        ((IToolProvider)burst).cti$setTool(toolStack);
        ItemStack dummyLens = DummyToolManaLens.getDummyLens(toolStack);
        burst.setSourceLens(dummyLens);
        ModifyBurstModifierHook.handleBurstCreation(burst,dummyLens,toolStack,player);
        calculatePowerAndEfficiency(burst,toolStack);
        burst.setStartingMana(burst.getMana());
        return burst;
    }

    public static void calculatePowerAndEfficiency(ManaBurstEntity burst, ToolStack toolStack){
        float power = toolStack.getStats().get(CtiToolStats.POWER);
        float efficiency = toolStack.getStats().get(CtiToolStats.EFFICIENCY);
        burst.setMana((int) (burst.getMana()*power));
        var burstExtra = (IManaBurstExtra) burst;
        burstExtra.addDamageModifier(power);
        burstExtra.cti$setPerConsumption((int) (burstExtra.cti$getPerConsumption()/efficiency));
        burstExtra.cti$setPerBlockConsumption((int) (burstExtra.cti$getPerConsumption()/efficiency));
    }

    @Override
    public void afterPlasmaSlashHit(ToolStack tool, LivingEntity target, PlasmaSlashEntity slash, boolean isCritical, float slashDamage) {
        if (tool.getModifierLevel(CtiModifiers.FAR_SIGHTS.get())<=0&&slash.getOwner() instanceof Player player&&slash.hitList.isEmpty()){
            var burst = getBurst(player, tool);
            if (ManaItemHandler.instance().requestManaExactForTool(tool.createStack(),player,burst.getMana(),true)
                    ||(tool.getVolatileData().getBoolean(KEY_MANA_RESONANCE)&&isSpellManaEnough(player, (int) Math.ceil(burst.getMana()*0.5f)))){
                launchBurst(tool,player,burst);
            }
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player!=null) {
            var burst = getBurst(player, (ToolStack) tool);
            tooltip.add(Component.translatable("tooltip.cti.mana_per_burst")
                    .append(""+burst.getMana()).withStyle(Style.EMPTY.withColor(0x73F5FF)));
            tooltip.add(Component.translatable("tooltip.cti.mana_per_entity")
                    .append(""+((IManaBurstExtra)burst).cti$getPerConsumption()).withStyle(Style.EMPTY.withColor(0x71D7FF)));
            tooltip.add(Component.translatable("tooltip.cti.mana_per_block")
                    .append(""+((IManaBurstExtra)burst).cti$getPerBlockConsumption()).withStyle(Style.EMPTY.withColor(0x71B2FF)));
            tooltip.add(Component.translatable("tooltip.cti.burst_time_before_loss")
                    .append(""+burst.getMinManaLoss())
                    .append(Component.translatable("tooltip.cti.burst_mana_loss"))
                    .append(burst.getManaLossPerTick()+"mana").withStyle(s->s.withColor(0x4192FF)));
        }
    }
}
