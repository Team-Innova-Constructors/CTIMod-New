package com.hoshino.cti.mixin.L2;

import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.xiaoyue.tinkers_ingenuity.utils.ToolUtils;
import dev.xkmc.l2hostility.content.traits.legendary.PushPullTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.List;

@Mixin(value = PushPullTrait.class, remap = false)
public abstract class RepellingMixin {
    @Inject(method = "tick", remap = false, at = {@At("HEAD")}, cancellable = true)
    private void tick(LivingEntity mob, int level, CallbackInfo ci) {
        if (mob.level.isClientSide()) {
            double x = mob.getX();
            double y = mob.getY();
            double z = mob.getZ();
            List<Player> lv = mob.level.getEntitiesOfClass(Player.class, new AABB(x + 10, y + 10, z + 10, x - 10, y - 10, z - 10));
            for (Player players : lv) {
                if (players != null) {
                    List<ItemStack> stacks = ToolUtils.Curios.getStacks(players);
                    for (ItemStack curios : stacks) {
                        if (ModifierUtil.getModifierLevel(curios, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId()) > 0) {
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }
}
