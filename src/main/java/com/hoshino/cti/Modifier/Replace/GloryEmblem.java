package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Cti;
import com.xiaoyue.tinkers_ingenuity.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class GloryEmblem extends Modifier implements MeleeHitModifierHook, ProjectileHitModifierHook, TooltipModifierHook {
    private final ResourceLocation EMBLEM = Cti.getResource("glory_emblem");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT,ModifierHooks.PROJECTILE_HIT,ModifierHooks.TOOLTIP);
    }
    private int getEmblem(IToolStackView view){
        return view.getPersistentData().getInt(EMBLEM);
    }
    private void setEmblem(IToolStackView view,int count){
        view.getPersistentData().putInt(EMBLEM,count);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var level=modifier.getLevel();
        var currentEmblem=getEmblem(tool);
        var target=context.getLivingTarget();
        var player=context.getPlayerAttacker();

        if(target==null||player==null)return;
        if(!context.isFullyCharged())return;
        var playerHealth=player.getMaxHealth();
        var targetHealth=target.getMaxHealth();
        if (currentEmblem < level * 40) {
            setEmblem(tool,currentEmblem+1);
        }
        if (getEmblem(tool) >= 10 && targetHealth > playerHealth * 10f) {
            setEmblem(tool,currentEmblem-10);
            var finalDamage=Math.min(targetHealth * 0.15f,500 * level) ;
            ToolAttackUtil.attackEntitySecondary(DamageSource.playerAttack(player).bypassArmor(), finalDamage, target, player, true);
        }
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            list.add(TooltipUtils.addTooltipWithValue("glory_emblem.attack_damage", getEmblem(iToolStackView)));
        }
    }
}
