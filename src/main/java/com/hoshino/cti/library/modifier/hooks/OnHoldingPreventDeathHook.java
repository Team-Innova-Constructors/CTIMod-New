package com.hoshino.cti.library.modifier.hooks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;


public interface OnHoldingPreventDeathHook {

    /**
     * 当手上工具有此词条,实体将要死亡时候会运行,运行成功会阻止目标死亡.
     * <br>
     * <ul>如果你想要在实体真正死亡时候触发效果,可以用这个
     *   <li>{@link OnDeathModifierHook}: 在实体死亡时候不触发免死但是可以正常执行其他,可以用于施加nbt或者亡语之类的,请注意,只有实体真正死了才会触发这个,如果通过{@link OnHoldingPreventDeathHook}中的方法免死了不会执行</li>
     * </ul>
     *
     * @param livingEntity 被作用的实体
     * @param tool         使用的工具
     * @param modifier     词条实例
     * @param context      触发这个语句的实体对应的装备的context
     * @param slotType     槽位种类
     * @param source       导致这次死亡的伤害来源
     * @return 阻止此次死亡后返还的生命值量, 如果为0则不会触发免死  <br> <strong>请记得一定要给方法来阻止一直免死,除非你很极端</strong> <br>下面是一个示例,用Nbt添加免死冷却的示例 <pre>{@code public ResourceLocation death= OrdinaryTinker.getResource("death");//引入ResourceLocation
     *
     *
     * @Override
     * //这里用onInventoryTick方法来达到每刻减一
     *     public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
     *         int coolDownTick=tool.getPersistentData().getInt(death);//获取工具冷却刻
     *         //如果大于0则每刻减一
     *         if(coolDownTick>0){
     *             tool.getPersistentData().putInt(death,coolDownTick-1);
     *         }
     *     }
     *     @Override
     *     public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
     *         int coolDownTick=tool.getPersistentData().getInt(death);//获取工具冷却刻
     *         if(coolDownTick==0){
     *             //death对应nbt值为0时候触发效果,施加nbt并且返还生命
     *             tool.getPersistentData().putInt(death,200);
     *             return 3;
     *         }
     *         return 0;
     *     }
     *
     *
     *
     * } </pre>
     */
    float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source);

    /**
     * 设置此此免死是否无视"BYPASSES_INVULNERABILITY"标签的伤害类型
     * <br>
     * <br><strong>默认不会无视,因为Kill伤害无法正常击杀实体是很严重的事情,如果更改了导致的一系列问题要做好善后</strong>
     * </ul>
     *
     * @return 是否无视 boolean
     */
    default boolean canIgnorePassInvul() {
        return false;
    }

    /**
     * The type First merger.
     */
    record  FirstMerger(Collection<OnHoldingPreventDeathHook> modules) implements OnHoldingPreventDeathHook {
        @Override
        public float onHoldingPreventDeath(LivingEntity livingEntity,IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
            for (OnHoldingPreventDeathHook module : modules) {
                float remainHealth = module.onHoldingPreventDeath(livingEntity,tool, modifier, context, slotType, source);
                if(remainHealth>1){
                    return remainHealth;
                }
            }
            return Float.MIN_VALUE;
        }

        @Override
        public boolean canIgnorePassInvul() {
            for (OnHoldingPreventDeathHook module : modules) {
                if (module.canIgnorePassInvul()) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * On holding prevent death float.
     *
     * @param hook         the hook
     * @param context      the context
     * @param source       the source
     * @param livingEntity the living entity
     * @return the float
     */
    static float onHoldingPreventDeath(ModuleHook<OnHoldingPreventDeathHook> hook, EquipmentContext context, DamageSource source, LivingEntity livingEntity) {
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    float healthRemain = entry.getHook(hook).onHoldingPreventDeath(livingEntity,toolStack, entry, context, slotType, source);
                    if (healthRemain > 0) {
                        return healthRemain;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Can ignore pass invul boolean.
     *
     * @param hook    the hook
     * @param context the context
     * @return the boolean
     */
    static boolean canIgnorePassInvul(ModuleHook<OnHoldingPreventDeathHook> hook, EquipmentContext context){
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            IToolStackView toolStack = context.getToolInSlot(slotType);
            if (toolStack != null && !toolStack.isBroken()) {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    return entry.getHook(hook).canIgnorePassInvul();
                }
            }
        }
        return false;
    }
}