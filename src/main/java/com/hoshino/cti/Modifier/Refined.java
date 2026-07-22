package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.hoshino.cti.Cti;
import com.hoshino.cti.util.DynamicColorEnum;
import com.marth7th.solidarytinker.register.SolidarytinkerItem;
import com.marth7th.solidarytinker.register.SolidarytinkerToolstats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class Refined extends EtSTBaseModifier implements VolatileDataModifierHook, ToolDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.VOLATILE_DATA);
        hookBuilder.addModule(new ArmorLevelModule(KEY_REFINE, false, null));
    }

    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_REFINE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("refined"));

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() != null) {
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                int level = cap.get(KEY_REFINE, 0);
                if (level > 0) {
                    float reduce = level * 0.08f * (event.getSource().isBypassMagic() ? 0.5f : 1f);
                    if (reduce > 0.8f) {
                        reduce = 0.8f;
                    }
                    event.setAmount(event.getAmount() * (1 - reduce));
                }
            });
        }
    }

    public static StatsGroup[] classicStatsArray = new StatsGroup[]{
            new StatsGroup(ToolStats.ARMOR, 0.25f),
            new StatsGroup(ToolStats.ARMOR_TOUGHNESS, 0.25f),
            new StatsGroup(ToolStats.ACCURACY, 0.1f),
            new StatsGroup(ToolStats.PROJECTILE_DAMAGE, 0.1f),
            new StatsGroup(ToolStats.DRAW_SPEED, 0.2f),
            new StatsGroup(ToolStats.MINING_SPEED, 0.3f),
            new StatsGroup(ToolStats.ATTACK_SPEED, 0.1f),
            new StatsGroup(ToolStats.ATTACK_DAMAGE, 0.2f),
            new StatsGroup(ToolStats.DURABILITY, 0.25f),
    };

    @Override
    public void modifierAddToolStats(IToolContext iToolContext, ModifierEntry modifier, ModifierStatsBuilder modifierStatsBuilder) {
        var modifierLevel = modifier.getLevel();
        int specialWeaponLevel = Math.min(modifierLevel, 5);
        Arrays.stream(classicStatsArray).forEach(s -> s.toolStat.multiply(modifierStatsBuilder, 1 + (s.number * modifierLevel)));
        etshtinkerToolStats.PLASMARANGE.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        etshtinkerToolStats.DAMAGEMULTIPLIER.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());
        etshtinkerToolStats.FLUID_EFFICIENCY.multiply(modifierStatsBuilder, 1 + 0.1 * modifier.getLevel());

        if (iToolContext.getItem() == SolidarytinkerItem.soulge.get()) {
            SolidarytinkerToolstats.ATTACK_FREQUENCY.multiply(modifierStatsBuilder, 1 + (-0.15f * specialWeaponLevel));
            SolidarytinkerToolstats.EXERT_TIMES.multiply(modifierStatsBuilder, 1 + (0.1f * specialWeaponLevel));
            SolidarytinkerToolstats.KILLTHRESHOLD.multiply(modifierStatsBuilder, 1 + (0.08f * specialWeaponLevel));
        }
        if (iToolContext.getItem() == SolidarytinkerItem.perception_rapier.get()) {
            SolidarytinkerToolstats.MAX_LEVEL.multiply(modifierStatsBuilder, 1 + (0.15f * specialWeaponLevel));
            SolidarytinkerToolstats.PERCEPTION_COUNT.multiply(modifierStatsBuilder, 1 + (0.1f * specialWeaponLevel));
        }
        if (iToolContext.getItem() == SolidarytinkerItem.ElectricBatons.get()) {
            SolidarytinkerToolstats.DETECTION_RANGE.multiply(modifierStatsBuilder, 1 + (0.2f * specialWeaponLevel));
        }
        SolidarytinkerToolstats.ENERGY_CAPACITY.multiply(modifierStatsBuilder, 1 + 0.25 * modifierLevel);
    }


    @Override
    public void addVolatileData(IToolContext iToolContext, ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        var level = modifierEntry.getLevel();
        modDataNBT.addSlots(SlotType.UPGRADE, level);
        modDataNBT.addSlots(SlotType.ABILITY, level);
        modDataNBT.addSlots(SlotType.DEFENSE, level);
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (RANDOM.nextInt(4) < modifier.getLevel()) {
            return 0;
        }
        return amount;
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return DynamicColorEnum.REFINED.buildNameComponent(getTranslationKey(), level, null, true);
    }

    public record StatsGroup(FloatToolStat toolStat, float number) {
    }
}
