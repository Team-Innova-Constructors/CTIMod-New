package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.Cti;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MessengerOfDawn extends Modifier implements InventoryTickModifierHook , OnAttackedModifierHook {
    private static final ResourceLocation LIMING_COOL_DOWN= Cti.getResource("liming_cooldown_time");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED);
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        if(getCooldown(tool)>0){
            return Component.translatable(getTranslationKey()).append(Component.literal("  黎明使者cd:"+getCooldown(tool))).withStyle(style -> style.withColor(getTextColor()));
        }
        return Component.translatable(getTranslationKey()).append(Component.literal("  黎明使者就绪:")).withStyle(style -> style.withColor(getTextColor()));
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(livingEntity.tickCount%20==0){
            if(getCooldown(tool)>0){
                setCooldown(tool,getCooldown(tool)-1);
            }
        }
    }
    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if(getCooldown(tool)>0)return;
        var entity=equipmentContext.getEntity();
        if(entity.getHealth()<entity.getMaxHealth() * 0.5f&&!EntityTickerManager.getInstance(entity).hasTicker(CtiEntityTickers.DAWN.get())){
            EntityTickerManager.getInstance(entity).addTicker(new EntityTickerInstance(CtiEntityTickers.DAWN.get(),1,60),Integer::max,Integer::max);
            EntityTickerManager.getInstance(entity).addTicker(new EntityTickerInstance(CtiEntityTickers.DAWN_EXTRA_DAMAGE.get(),modifierEntry.getLevel(),600),Integer::max,Integer::max);
            setCooldown(tool,180);
        }
    }
    private static int getCooldown(IToolStackView tool){
        return tool.getPersistentData().getInt(LIMING_COOL_DOWN);
    }
    private static void setCooldown(IToolStackView tool,int time){
        tool.getPersistentData().putInt(LIMING_COOL_DOWN,time);
    }
}
