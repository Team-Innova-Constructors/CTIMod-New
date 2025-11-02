package com.hoshino.cti.Modifier.aetherCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.armor.BlockDamageSourceModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.hoshino.cti.Modifier.aetherCompact.AmbrosiumPowered.KEY_AMBROSIUM_POWER;

public class Phoenix extends EtSTBaseModifier {

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new BlockDamageSourceModule(DamageSourcePredicate.FIRE, ModifierCondition.ANY_TOOL));
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
