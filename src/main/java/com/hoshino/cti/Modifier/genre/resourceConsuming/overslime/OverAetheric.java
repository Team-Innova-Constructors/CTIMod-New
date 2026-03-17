package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.content.materialGenre.GenreManager;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.concurrent.atomic.AtomicInteger;

import static com.hoshino.cti.Modifier.aetherCompact.Atheric.TAG_AETHER;

public class OverAetheric extends BasicOverslimeModifier {
    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*250;
    }
    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*10;
    }
    @Override
    public float getDamageBase(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*20;
    }
    @Override
    public float getDamageMul(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*0.2f;
    }

    @Override
    public float getArmorBase(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*20;
    }

    @Override
    public float getArmorMul(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*0.2f;
    }

    public static int getAetherCount(MaterialNBT nbt){
        AtomicInteger count = new AtomicInteger();
        nbt.getList().forEach(materialVariant -> {
            if (MaterialRegistry.getInstance().isInTag(materialVariant.getId(),TAG_AETHER)) count.addAndGet(1);
        });
        return count.get();
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context, modifier, builder);
        var count = getAetherCount(context.getMaterials());
        if (count>0){
            ToolStats.DURABILITY.percent(builder,count*0.1f);
            OverslimeModifier.OVERSLIME_STAT.percent(builder,count*0.1f);
        }
    }
}
