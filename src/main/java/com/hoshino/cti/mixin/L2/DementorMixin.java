package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.traits.legendary.DementorTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DementorTrait.class, remap = false)
public class DementorMixin {
    /**
     * @author FireFly
     * @reason 摄魂判定问题，此形参无法正确检测isBypassArmor属性,因此mixin掉,不再免疫
     */
    @Overwrite
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
    }
    /**
     * @author firefly
     * @reason 不让反射伤害被修改
     */
    @Overwrite
    public void onCreateSource(int level, LivingEntity attacker, LivingAttackEvent event) {
        if(event.getSource().getMsgId().equals("mobattackreflect"))return;
        event.getSource().bypassArmor();
    }
}
