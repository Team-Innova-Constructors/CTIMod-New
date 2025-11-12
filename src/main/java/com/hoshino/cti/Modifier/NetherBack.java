package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class NetherBack extends Modifier implements InventoryTickModifierHook , MeleeDamageModifierHook , TooltipModifierHook {
    public static final ResourceLocation NETHER_GAS = Cti.getResource("nether_gas");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.MELEE_DAMAGE,ModifierHooks.TOOLTIP);
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float v, float v1) {
        if(getNE(iToolStackView)<4444)return v1;
        setNE(iToolStackView,0);
        return v1 * 88.888f;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(!(livingEntity instanceof Player player))return;
        if(player.tickCount%20!=0)return;
        var currentNE=getNE(iToolStackView);
        if(currentNE<4444){
            setNE(iToolStackView,currentNE+1);
        }

    }
    public int getNE(IToolStackView view) {
        return view.getPersistentData().getInt(NETHER_GAS);
    }

    public void setNE(IToolStackView view, int amount) {
        view.getPersistentData().putInt(NETHER_GAS, amount);
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        var c= getNE(iToolStackView);
        var process=c/4444f * 100f;
        list.add(Component.literal("当前怨气进度"+":"+process+"%").withStyle(style -> style.withColor(0xff171a)));
    }
}
