package com.hoshino.cti.mixin.TconMixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.combat.MeleeAttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;

@Mixin(value = MeleeAttributeModule.class,remap = false)
public class MeleeAttributeModuleMixin {
    @Shadow
    @Final
    private ModifierCondition<IToolStackView> condition;

    @Shadow
    @Final
    private Attribute attribute;

    @Shadow
    @Final
    private UUID uuid;

    @Shadow
    @Final
    private String unique;

    @Shadow
    @Final
    private LevelingValue amount;

    @Shadow
    @Final
    private AttributeModifier.Operation operation;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (this.condition.matches(tool, modifier)) {
            LivingEntity target = context.getLivingTarget();
            if (target != null) {
                AttributeInstance instance = target.getAttribute(this.attribute);
                if (instance != null&&instance.getModifier(uuid)==null) {
                    instance.addTransientModifier(new AttributeModifier(this.uuid, this.unique, this.amount.compute(modifier.getEffectiveLevel()), this.operation));
                }
            }
        }
        return knockback;
    }
}
