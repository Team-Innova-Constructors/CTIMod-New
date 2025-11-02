package com.hoshino.cti.Modifier;

import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PAttackSelfC2S;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.LogicalSide;
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

public class DoubleEdged extends NoLevelsModifier implements MeleeDamageModifierHook, MeleeHitModifierHook {
    public DoubleEdged() {
        MinecraftForge.EVENT_BUS.addListener(this::OnLeftClick);
        MinecraftForge.EVENT_BUS.addListener(this::OnLeftClickBlock);
    }

    private void OnLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getSide() == LogicalSide.CLIENT) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof IModifiable && event.getHand() == InteractionHand.MAIN_HAND) {
                ToolStack tool = ToolStack.from(stack);
                if (tool.getModifierLevel(this) > 0) {
                    CtiPacketHandler.sendToServer(new PAttackSelfC2S());
                }
            }
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.MELEE_HIT);
    }

    private void OnLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getSide() == LogicalSide.CLIENT) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof IModifiable && event.getHand() == InteractionHand.MAIN_HAND) {
                ToolStack tool = ToolStack.from(stack);
                if (tool.getModifierLevel(this) > 0) {
                    CtiPacketHandler.sendToServer(new PAttackSelfC2S());
                }
            }
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (!context.isFullyCharged() && !context.isExtraAttack()) {
            ToolAttackUtil.attackEntity(tool, context.getAttacker(), context.getHand(), context.getAttacker(), context::getCooldown, true);
        }
        return knockback;
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float baseDamage, float damage) {
        if (toolAttackContext.getHand() == InteractionHand.MAIN_HAND && toolAttackContext.isFullyCharged()) {
            return damage * 2.5f;
        }
        return damage;
    }
}
