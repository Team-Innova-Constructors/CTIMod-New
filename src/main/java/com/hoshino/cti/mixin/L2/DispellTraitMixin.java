package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.ILivingEntityMixin;
import com.hoshino.cti.util.L2.ExHurtHelper;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.xiaoyue.tinkers_ingenuity.utils.ToolUtils;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.traits.legendary.DispellTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import mekanism.common.registries.MekanismItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.List;

@Mixin(value = DispellTrait.class, remap = false)
public class DispellTraitMixin extends LegendaryTrait {
    public DispellTraitMixin(ChatFormatting format) {
        super(format);
    }

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
    /**
     * @author <h3>莹</h3>
     * <h5>增强破魔,摄魂,使其依照怪物等级造成额外伤害</h5>
     * <br><h5>I.当怪物造成伤害时,额外对玩家造成一次自身莱特兰等级2% * 词条等级的穿刺伤害,此伤害无视大多数免伤</h5>
     * <h5>II.当盔甲上有超坚硬时候,降低20%受到的此伤害,只计算一级,单次</h5>
     * <h5>III.有遗者词条时候,依据全身等级,每级降低该伤害3%</h5>
     * <h5>IV.有维格利得之魂词条时,依据全身等级,每级再次降低该伤害7%</h5>
     * <h5>V.有星野戒指/无尽,星河马玉灵,恐怖星野盔甲/无尽套,mekasuit时无视此效果</h5>
     */

    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        if(ExHurtHelper.shouldHurt(target)){
            var source=new EntityDamageSource("dispell",attacker);
            var mobLevel = DifficultyLevel.ofAny(attacker);
            float scale = ExHurtHelper.getScale(target);
            float totalHurt=mobLevel * 0.02f * level *scale;
            if(totalHurt==0)return;
            if(target.isDeadOrDying()||!target.isAlive())return;
            target.invulnerableTime=0;
            ((ILivingEntityMixin)target).cti$strictHurt(source,totalHurt,false);
        }
    }

}
