package com.hoshino.cti.library.modifier.hooks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface OnDeathModifierHook {
    /**
     * 在实体真正死亡的时候生效该方法
     * <br>如果被{@link OnHoldingPreventDeathHook}阻止死亡,<strong>则不会触发</strong>
     * <br>
     * @param tool 工具实例
     * @param modifier 词条实例
     * @param context EquipmentContext对应的context
     * @param slotType 槽位类型
     * @param source 伤害源
     * @param victim 被击杀者
     * @param isAliveSource 伤害是否有来源
     */
    void onDeathed(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, LivingEntity victim, boolean isAliveSource);
    record AllMerger(Collection<OnDeathModifierHook> modules) implements OnDeathModifierHook {
        @Override
        public void onDeathed(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, LivingEntity victim,boolean isAliveSource) {
            for (OnDeathModifierHook module : modules) {
                module.onDeathed(tool, modifier, context, slotType, source, victim,isAliveSource);
            }
        }
    }

    /**
     *检测伤害是否有源
     */
    static boolean isAliveSource(DamageSource source) {
        return source.getEntity() != null;
    }
    static void handleDeath(ModuleHook<OnDeathModifierHook> hook, EquipmentContext context, DamageSource source, LivingEntity victim) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            handleDeath(hook, context, source,victim,slotType);
        }
    }
    private static void handleDeath(ModuleHook<OnDeathModifierHook> hook, EquipmentContext context, DamageSource source, LivingEntity victim, EquipmentSlot slotType) {
        IToolStackView toolStack = context.getToolInSlot(slotType);
        if (toolStack != null && !toolStack.isBroken()) {
            for (ModifierEntry entry : toolStack.getModifierList()) {
                entry.getHook(hook).onDeathed(toolStack,entry,context,slotType,source,victim,isAliveSource(source));
            }
        }
    }
}
