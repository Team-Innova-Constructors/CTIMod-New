package com.hoshino.cti.integration.botania.tool;

import com.hoshino.cti.integration.botania.api.hook.BurstHitModifierHook;
import com.hoshino.cti.integration.botania.api.hook.UpdateBurstModifierHook;
import com.hoshino.cti.register.CtiItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.LensEffectItem;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.item.lens.Lens;

import java.util.ArrayList;
import java.util.List;

public class DummyToolManaLens extends ModifiableItem implements LensEffectItem {
    public DummyToolManaLens() {
        super(new Properties(), ToolDefinition.EMPTY);
    }

    @Override
    public void updateBurst(ManaBurst burst, ItemStack stack) {
        if (stack.getItem() instanceof IModifiable){
            UpdateBurstModifierHook.handleBurstUpdate(burst,stack);
        }
    }

    public static ItemStack getDummyLens(ToolStack tool){
        var itemStack = new ItemStack(CtiItem.DUMMY_TOOL_MANA_LENS.get(),1);
        itemStack.setTag(tool.createStack().getTag());
        return itemStack;
    }

    @Override
    public int getManaToTransfer(ManaBurst burst, ItemStack stack, ManaReceiver receiver) {
        return LensEffectItem.super.getManaToTransfer(burst, stack, receiver);
    }

    @Override
    public void apply(ItemStack stack, BurstProperties props, Level level) {
    }

    @Override
    public boolean collideBurst(ManaBurst burst, HitResult result, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
        return BurstHitModifierHook.handleBurstHit(burst,result,isManaBlock,shouldKill,stack);
    }

    @Override
    public boolean doParticles(ManaBurst burst, ItemStack stack) {
        return true;
    }
}
