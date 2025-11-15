package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.common.AdaptingTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AdaptingTrait.class)
public class AdaptingMixin extends MobTrait {
    public AdaptingMixin(ChatFormatting format) {
        super(format);
    }

    /**
     * @author firefly
     * @reason
     * <h3>最新更改: 适应的平滑化</h3>
     * <h5>I.取消掉了原本的攻击次数变多免伤也叠加
     * <br>II.新增基础免伤90%,每承受过多一种伤害,最终免伤都会削减为 90% ÷ 伤害种类 × √词条等级
     * <br>III.适应等级上限提升到3级,但免伤上限仍然为90%
     * <br>IV.保留曾经的穿魔,穿无敌免伤失效,额外增加穿甲伤害降低50%原有免伤</h5>
     */
    @Overwrite(remap = false)
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().isBypassMagic() || event.getSource().isBypassInvul()) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(entity);
        AdaptingTrait.Data data = cap.getOrCreateData(this.getRegistryName(), AdaptingTrait.Data::new);
        String id = event.getSource().getMsgId();
        boolean isBypassArmor=event.getSource().isBypassArmor();
        int damageTypesAmount = data.memory.size();
        if (data.memory.contains(id)) {
            data.memory.remove(id);
            data.memory.add(0, id);
            float levelScale=(float)Math.sqrt(level);
            float baseMultiple=0.9f / damageTypesAmount * levelScale;
            float finallyMultiple=isBypassArmor?baseMultiple * 0.9f:baseMultiple ;
            event.setAmount(event.getAmount() * (1-Math.min(0.9f,finallyMultiple)));
        } else {
            data.memory.add(0, id);
            data.adaption.put(id, 1);
        }
    }
}
