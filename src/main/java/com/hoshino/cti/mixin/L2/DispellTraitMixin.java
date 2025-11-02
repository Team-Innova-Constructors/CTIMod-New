package com.hoshino.cti.mixin.L2;

import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.xiaoyue.tinkers_ingenuity.utils.ToolUtils;
import dev.xkmc.l2hostility.content.traits.legendary.DispellTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.List;

@Mixin(value = DispellTrait.class, remap = false)
public class DispellTraitMixin {
    @Inject(at = {@At("HEAD")}, method = {"postHurtImpl"}, cancellable = true)
    public void DispellMixin(int level, LivingEntity attacker, LivingEntity target, CallbackInfo ci) {
        if (target instanceof Player player) {
            List<ItemStack> curio = ToolUtils.Curios.getStacks(player);
            for (ItemStack curios : curio) {
                if (ModifierUtil.getModifierLevel(curios, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId()) > 0) {
                    ci.cancel();
                }
            }
        }
    }
    /**
     * @author firefly
     * @reason 不让反射伤害被修改
     */
    @Overwrite
    public void onCreateSource(int level, LivingEntity attacker, LivingAttackEvent event) {
        if(event.getSource().getMsgId().equals("mobattackreflect"))return;
        event.getSource().bypassMagic();
    }

    /**
     * @author firefly
     * @reason 破魔判定问题，此形参无法正确检测isBypassMagic属性,因此mixin掉,不再免疫
     */
    @Overwrite
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
    }
}
