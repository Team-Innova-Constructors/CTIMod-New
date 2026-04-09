package com.hoshino.cti.integration.botania.modifiers;

import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class FartherSights extends NoLevelsModifier implements AttributesModifierHook, ModifyBurstModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES, CtiBotModifierHooks.MODIFY_BURST);
    }
    public static final UUID FARTHER_SIGHTS_UUID = UUID.fromString("84ce90d7-45e2-a102-75e1-86867f0e7e1e");
    //当魔力脉冲带有这个Tag时，触发工具攻击/挖掘。
    //很遗憾这个东西目前我只能做到硬编码。
    public static final String KEY_TRIGGER_TOOL = "trigger_tinker_tool";

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (slot==EquipmentSlot.MAINHAND){
            List.of(ForgeMod.REACH_DISTANCE.get(),ForgeMod.ATTACK_RANGE.get()).forEach(attribute ->
                    consumer.accept(attribute,new AttributeModifier(FARTHER_SIGHTS_UUID,"desc.attribute.father_sights",-Float.MAX_VALUE, AttributeModifier.Operation.ADDITION)));
        }
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        if (owner instanceof Player player&&player.getMainHandItem().getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(player.getMainHandItem());
            if (toolStack.getModifierLevel(this)>0){
                burst.entity().addTag(KEY_TRIGGER_TOOL);
                burst.setMana(burst.getMana()+100);
                burstExtras.cti$setPerBlockConsumption(burstExtras.cti$getPerBlockConsumption()+50);
            }
        }
    }
}
