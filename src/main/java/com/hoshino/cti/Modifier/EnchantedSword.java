package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Entities.enchantedswordentity;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerEntity;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class EnchantedSword extends EtSTBaseModifier implements LeftClickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    @Override
    public boolean isNoLevels() {
         return true;
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        summonEnchantedSword(tool,player,level);
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (context.isFullyCharged()) summonEnchantedSword(tool,context.getPlayerAttacker(),context.getAttacker().level);
        return knockback;
    }

    public static void summonEnchantedSword(IToolStackView tool, Player player, Level level){
        if (!level.isClientSide&&player!=null&&player.getAttackStrengthScale(0)>0.9){
            enchantedswordentity entity = new enchantedswordentity(etshtinkerEntity.enchantedswordentity.get(),level);
            entity.setOwner(player);
            entity.setPos(player.getEyePosition());
            entity.shoot(player.getLookAngle().x,player.getLookAngle().y,player.getLookAngle().z,2,0);
            entity.damage=tool.getStats().get(ToolStats.ATTACK_DAMAGE);
            level.addFreshEntity(entity);
        }
    }
}
