package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.RepairFactorModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class IndustrialArmor extends EtSTBaseModifier implements VolatileDataModifierHook, ToolDamageModifierHook, RepairFactorModifierHook {
    @Override
    public float getRepairFactor(IToolStackView tool, ModifierEntry entry, float factor) {
        return factor*(1+entry.getLevel()*0.05f);
    }
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_INDUSTRIAL = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("industrial"));

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if (event.getEntity()!=null&&!event.getSource().isBypassArmor()) event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
            int level = cap.get(KEY_INDUSTRIAL,0);
            if (level>0){
                level = Math.min(16,level);
                event.setAmount(event.getAmount()*(1-level*0.05f));
            }
        });
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.VOLATILE_DATA);
        builder.addHook(this,ModifierHooks.TOOL_DAMAGE);
        builder.addHook(this,ModifierHooks.REPAIR_FACTOR);
        builder.addModule(new ArmorLevelModule(KEY_INDUSTRIAL,false,null));
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        volatileData.addSlots(SlotType.ABILITY,1);
        volatileData.addSlots(SlotType.DEFENSE,1);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.addToolStats(context, modifier, builder);
        ToolStats.ARMOR.percent(builder,0.05*modifier.getLevel());
        ToolStats.ARMOR_TOUGHNESS.percent(builder,0.05*modifier.getLevel());
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (RANDOM.nextFloat()>Math.min(0.8,modifier.getLevel()*0.05)) return 0;
        if (amount>2) amount=2;
        return amount;
    }
}
