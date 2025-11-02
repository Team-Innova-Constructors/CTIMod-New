package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Cti;
import com.xiaoyue.tinkers_ingenuity.generic.XIRModifier;
import com.xiaoyue.tinkers_ingenuity.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nullable;
import java.util.List;

public class OverwriteSoulDevouring extends XIRModifier implements ModifierRemovalHook {
    private final ResourceLocation KEY = Cti.getResource("soul_devouring");

    public OverwriteSoulDevouring() {
    }

    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.REMOVE);
    }

    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        this.getData(tool).remove(this.KEY);
        return null;
    }

    private float getBonus(IToolStackView tool, int level) {
        return this.getData(tool).getFloat(this.KEY) * 0.1F * (float) level;
    }

    public float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        return this.getData(tool).contains(this.KEY, 5) ? actualDamage * (1.0F + this.getBonus(tool, level)) : actualDamage;
    }

    public void onTinkerArrowShoot(IToolStackView bow, int level, LivingEntity shooter, Projectile projectile, AbstractArrow arrow, NamespacedNBT namespacedNBT, boolean primary) {
        if (this.getData(bow).contains(this.KEY, 5)) {
            arrow.setBaseDamage(arrow.getBaseDamage() * (double) (1.0F + this.getBonus(bow, level)));
        }
    }

    public void onKillTarget(IToolStackView tool, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
        if (event.getSource().getEntity() instanceof AbstractArrow) {
            this.getData(tool).putFloat(this.KEY, this.getData(tool).getFloat(this.KEY) + 0.1F * (float) level);
        }
    }

    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (target.isDeadOrDying()) {
            if (attacker instanceof FakePlayer) {
                return;
            } else {
                this.getData(tool).putFloat(this.KEY, this.getData(tool).getFloat(this.KEY) + 0.1F);
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
        if (player != null && this.getData(tool).contains(this.KEY, 5)) {
            TooltipModifierHook.addPercentBoost(this, TooltipUtils.addTooltip("soul_devouring.attack_damage"), (double) (this.getData(tool).getFloat(this.KEY) * 0.1F * modifier.getLevel()), list);
        }
    }
}
