package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.concurrent.atomic.AtomicInteger;

public class CoronationBurnt extends BasicBurntModifier {

    //施加灼伤时若目标灼伤层数已达上限，扣除目标25%(10%)灼伤层数并为玩家叠加一层灼伤增幅
    public static void tryAblate(Player cause, @Nullable EntityTickerInstance fieryInstance, int maxBurnt, @Nullable IToolStackView tool) {
        if (fieryInstance == null || maxBurnt <= 0 || fieryInstance.level < maxBurnt) return;
        int modifierLevel = getCoronationLevel(cause, tool);
        if (modifierLevel <= 0) return;
        AtomicInteger elementalReleaseLv = new AtomicInteger(0);
        cause.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE, 0)));
        float ratio = GenreManager.calculateElementalRelease(elementalReleaseLv.get(), 0.25f, 0.1f);
        fieryInstance.level -= Math.min(fieryInstance.level, Math.max(1, (int) (fieryInstance.level * ratio)));
        int maxStacks = 10 * modifierLevel;
        var manager = EntityTickerManager.getInstance(cause);
        var instance = manager.getTicker(CtiEntityTickers.CORONATION_BURNT.get());
        int stacks = Math.min((instance != null ? instance.level : 0) + 1, maxStacks);
        manager.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.CORONATION_BURNT.get(), stacks, 1200));
    }

    public static int getCoronationLevel(Player cause, @Nullable IToolStackView tool) {
        if (tool != null) return tool.getModifierLevel(CtiModifiers.CORONATION_BURNT.getId());
        ItemStack mainHand = cause.getMainHandItem();
        if (mainHand.getItem() instanceof IModifiable) {
            return ToolStack.from(mainHand).getModifierLevel(CtiModifiers.CORONATION_BURNT.getId());
        }
        return 0;
    }
}
