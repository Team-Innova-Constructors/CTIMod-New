package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.register.CtiAttributes;
import com.xiaoyue.tinkers_ingenuity.content.library.context.CurioAttributeContext;
import com.xiaoyue.tinkers_ingenuity.content.library.tools.IngenuityHooks;
import com.xiaoyue.tinkers_ingenuity.generic.Interface.curio.CurioAddonHook;
import com.xiaoyue.tinkers_ingenuity.generic.Interface.curio.CurioBuilderHook;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public class ShineFlame extends NoLevelsModifier implements CurioBuilderHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, IngenuityHooks.CURIO_BUILDER_HOOK);
    }

    @Override
    public void addCurioAttribute(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack stack, CurioAttributeContext attr) {
        attr.mulBaseAttr(CtiAttributes.BURNT_INFLICT.get(),0.6f);
    }
}
