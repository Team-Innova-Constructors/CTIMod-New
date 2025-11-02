package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.content.library.tools.TIToolStats;
import com.xiaoyue.tinkers_ingenuity.generic.XICModifier;
import com.xiaoyue.tinkers_ingenuity.modifiers.curio.CurioExquisite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

@Mixin(value = CurioExquisite.class, remap = false)
public class CurioExquisiteMixin extends XICModifier {
    /**
     * @author firefly
     * @reason 加算有铸币写成乘算了
     */
    @Overwrite
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        TIToolStats.ARMOR.add(builder, 0.1F * (float) modifier.getLevel());
        TIToolStats.ARMOR_TOUGHNESS.add(builder, 0.1F * (float) modifier.getLevel());
    }
}
