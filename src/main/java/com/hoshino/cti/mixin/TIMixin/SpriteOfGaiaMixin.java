package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.compat.botania.SpriteOfGaia;
import com.xiaoyue.tinkers_ingenuity.utils.LoadingUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;

@Mixin(value = SpriteOfGaia.class, remap = false)
public class SpriteOfGaiaMixin {
    /**
     * @author Firefly
     * @reason 额外伤害给的凋零伤而且明显过高
     * @数值调整 50%->30%
     */
    @Overwrite
    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (LoadingUtils.isLoadBot() && attacker instanceof Player player) {
            if (ManaItemHandler.instance().requestManaExact(attacker.getMainHandItem(), player, 160, true)) {
                ToolAttackUtil.attackEntitySecondary(DamageSource.playerAttack(player), damageDealt * (float) level * 0.3F, target, attacker, false);
            }
        }
    }
}
