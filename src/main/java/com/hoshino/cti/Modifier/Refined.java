package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.hoshino.cti.Cti;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class Refined extends EtSTBaseModifier implements VolatileDataModifierHook, ToolDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.VOLATILE_DATA);
        hookBuilder.addModule(new ArmorLevelModule(KEY_REFINE,false,null));
    }
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_REFINE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource(""));

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if (event.getEntity()!=null){
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
                int level = cap.get(KEY_REFINE,0);
                if (level>0){
                    level = Math.min(level,6);
                    float reduce = level*0.1f*(event.getSource().isBypassArmor()?0.5f:1f);
                    event.setAmount(event.getAmount()*(1-reduce));
                }
            });
        }
    }

    @Override
    public void modifierAddToolStats(IToolContext iToolContext, ModifierEntry modifier, ModifierStatsBuilder modifierStatsBuilder) {
        ToolStats.ARMOR.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.ARMOR_TOUGHNESS.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.ACCURACY.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.PROJECTILE_DAMAGE.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.MINING_SPEED.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.ATTACK_SPEED.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.ATTACK_DAMAGE.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.BLOCK_ANGLE.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.BLOCK_AMOUNT.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.DURABILITY.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        ToolStats.DRAW_SPEED.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());

        etshtinkerToolStats.PLASMARANGE.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        etshtinkerToolStats.DAMAGEMULTIPLIER.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        etshtinkerToolStats.FLUID_EFFICIENCY.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());

        ToolTankHelper.CAPACITY_STAT.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
    }


    @Override
    public void addVolatileData(IToolContext iToolContext, ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        modDataNBT.addSlots(SlotType.UPGRADE, modifierEntry.getLevel());
        modDataNBT.addSlots(SlotType.ABILITY, modifierEntry.getLevel());
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (RANDOM.nextInt(4)<modifier.getLevel()){
            return 0;
        }
        return amount;
    }
}
