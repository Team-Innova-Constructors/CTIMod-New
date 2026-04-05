package com.hoshino.cti.Modifier.aetherCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.armor.BlockDamageSourceModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.hoshino.cti.Modifier.aetherCompact.AmbrosiumPowered.KEY_AMBROSIUM_POWER;

public class Phoenix extends EtSTBaseModifier implements ModifyDamageModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new BlockDamageSourceModule(DamageSourcePredicate.FIRE, ModifierCondition.ANY_TOOL));
        builder.addHook(this, ModifierHooks.MODIFY_HURT);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.msgId.equals(IafDamageRegistry.DRAGON_FIRE_TYPE)||
            source.msgId.equals(IafDamageRegistry.DRAGON_ICE_TYPE)||source.msgId.equals(IafDamageRegistry.DRAGON_LIGHTNING_TYPE))
            amount*=0.5f;
        return amount;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide&&level.getGameTime()%20==0){
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = holder.getItemBySlot(slot);
                if (stack.getItem() instanceof IModifiable){
                    ToolStack toolStack = ToolStack.from(stack);
                    int modifierLevel =toolStack.getModifierLevel(CtiModifiers.AMBROSIUM_POWERED.get());
                    if (modifierLevel>0&&toolStack.getPersistentData().getInt(KEY_AMBROSIUM_POWER)<24*modifierLevel){
                        AmbrosiumPowered.chargeTool(toolStack);
                    }
                }
            }
        }
    }
}
