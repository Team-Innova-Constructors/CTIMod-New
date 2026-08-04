package com.hoshino.cti.Modifier.Replace;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.generic.XIRModifier;
import com.xiaoyue.tinkers_ingenuity.utils.ItemUtils;
import com.xiaoyue.tinkers_ingenuity.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.function.BiConsumer;

public class FixSanguine extends XIRModifier {
    private final ResourceLocation KEY = TinkersIngenuity.location("sanguine");

    public FixSanguine() {
    }

    private float getBonus(IToolStackView tool) {
        return (float) this.getData(tool).getInt(this.KEY) * 0.01F;
    }

    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.DURABILITY.add(builder, -0.15F * modifier.getLevel());
    }

    public void onTakeDamage(IToolStackView tool, LivingDamageEvent event, DamageSource source, LivingEntity entity, int level) {
        if (event.getAmount() > 5.0F && RANDOM.nextBoolean() && this.getData(tool).getInt(this.KEY) < 50 * level) {
            this.getData(tool).putInt(this.KEY, this.getData(tool).getInt(this.KEY) + 1);
        }
    }

    public float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        return actualDamage * (1.0F + this.getBonus(tool));
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, NamespacedNBT namespacedNBT, boolean primary) {
        if (arrow!=null){
            arrow.setBaseDamage(arrow.getBaseDamage()+(1.0F + this.getBonus(tool)*0.5f));
        }
    }

    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (slot == EquipmentSlot.MAINHAND) {
            consumer.accept(Attributes.ATTACK_SPEED, ItemUtils.mulBaseAttr(slot, ItemUtils.getAttrName("sanguine", slot), this.getBonus(tool)*0.5));
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
        if (key == TooltipKey.SHIFT) {
            list.add(TooltipUtils.addTooltipWithValue("sanguine.keys", this.getData(tool).getInt(this.KEY)));
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.getGameTime()%400==0&&this.getData(tool).getInt(this.KEY)>0)
            this.getData(tool).putInt(this.KEY, this.getData(tool).getInt(this.KEY) - 1);
    }
}
