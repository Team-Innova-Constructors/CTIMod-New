package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Modifier.aetherCompact.AmbrosiumPowered;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.concurrent.atomic.AtomicInteger;

import static com.hoshino.cti.Modifier.aetherCompact.Atheric.TAG_AETHER;

public class OverAetheric extends BasicOverslimeModifier implements VolatileDataModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.VOLATILE_DATA);
    }

    public static final ResourceLocation KEY_AETHER_COUNT = Cti.getResource("over_aetheric_count");
    public static final ResourceLocation KEY_CHARGE = Cti.getResource("over_aetheric_charge");
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
        return modifier.getLevel()*25;
    }
    @Override
    public float getDamageMul(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*0.4f;
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
    public static int getAetherCount(IToolStackView tool){
        return tool.getVolatileData().getInt(KEY_AETHER_COUNT);
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

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (tool.getPersistentData().contains(KEY_CHARGE, Tag.TAG_INT)){
            AmbrosiumPowered.chargeLiving(holder,tool.getPersistentData().getInt(KEY_CHARGE));
            tool.getPersistentData().remove(KEY_CHARGE);
        }
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        volatileData.putInt(KEY_AETHER_COUNT,getAetherCount(context.getMaterials()));
    }
}
