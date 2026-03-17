package com.hoshino.cti.Modifier.genre.insatiable;

import com.hoshino.cti.Modifier.genre.insatiable.forTrait.InsatiableHandler;
import com.hoshino.cti.register.CtiAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ToolType;

public class UnitedInsatiable extends BasicInsatiableModifier{
    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 32*modifier.getLevel();
    }

    @Override
    public int getInsatiableLevel() {
        return 4;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide&&level.getGameTime()%5==0&&isCorrectSlot){
            var insatiableLevel = TinkerModifiers.insatiableEffect.get(ToolType.MELEE).getLevel(holder);
            var os = TinkerModifiers.overslime.get();
            var instance = holder.getAttribute(CtiAttributes.MAX_INSATIABLE.get());
            var targetLevel = 0;
            if (instance!=null)
                targetLevel+=(int) instance.getValue()/2;
            if (targetLevel<=0) return;
            if (insatiableLevel<=0&&os.getShield(tool)>=100){
                os.addOverslime(tool,modifier,-100);
                TinkerModifiers.insatiableEffect.get(ToolType.MELEE).apply(holder, InsatiableHandler.EFFECT_TICKS,targetLevel,true);
            } else if (insatiableLevel<=targetLevel&&level.getGameTime()%20==0&&os.getShield(tool)>0){
                os.addOverslime(tool,modifier,-1);
                TinkerModifiers.insatiableEffect.get(ToolType.MELEE).apply(holder, InsatiableHandler.EFFECT_TICKS,targetLevel,true);
            }
        }
    }
}
