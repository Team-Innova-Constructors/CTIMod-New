package com.hoshino.cti.Modifier.aetherCompact;

import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import com.hoshino.cti.util.CommonUtil;
import com.hoshino.cti.util.DamageSourceUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class Atheric extends EtSTBaseModifier implements EnchantmentModifierHook , VolatileDataModifierHook , AttributesModifierHook {
    public static final TagKey<IMaterial> TAG_AETHER = MaterialManager.getTag(Cti.getResource("aether"));
    public static final ResourceLocation KEY_ATHERIC = Cti.getResource("atheric");

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this,ModifierHooks.ENCHANTMENTS,ModifierHooks.VOLATILE_DATA,ModifierHooks.ATTRIBUTES);
    }

    public int getBonus(IToolStackView tool){
        return tool.getVolatileData().getInt(KEY_ATHERIC);
    }

    @Override
    public int updateEnchantmentLevel(IToolStackView tool, ModifierEntry modifierEntry, Enchantment enchantment, int level) {
        int bonus = getBonus(tool);
        if (bonus>0&&(enchantment== Enchantments.MOB_LOOTING||enchantment==Enchantments.BLOCK_FORTUNE)){
            level+=bonus;
        }
        return level;
    }

    @Override
    public void updateEnchantments(IToolStackView tool, ModifierEntry modifierEntry, Map<Enchantment, Integer> map) {
        int bonus = getBonus(tool);
        if (bonus>0){
            EnchantmentModifierHook.addEnchantment(map, Enchantments.BLOCK_FORTUNE,bonus);
            EnchantmentModifierHook.addEnchantment(map, Enchantments.MOB_LOOTING,bonus);
        }
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return damage*(1+0.5f*getBonus(tool));
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean primary) {
        if (abstractArrow!=null){
            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage()*(1+0.20f*getBonus(tool)));
        }
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        AtomicInteger count = new AtomicInteger();
        iToolContext.getMaterials().getList().forEach(materialVariant -> {
            if (MaterialRegistry.getInstance().isInTag(materialVariant.getId(),TAG_AETHER)) count.addAndGet(1);
        });
        modDataNBT.putInt(KEY_ATHERIC,count.get());
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return super.getDisplayName().copy().append(Component.literal(" +" + getBonus(tool)));
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        int bonus = getBonus(tool);
        List<Attribute> attributes = List.of(Attributes.LUCK,Attributes.KNOCKBACK_RESISTANCE, ForgeMod.REACH_DISTANCE.get(),Attributes.MAX_HEALTH);
        for (Attribute value : attributes) {
            if (bonus > 0) {
                consumer.accept(value, new AttributeModifier(CommonUtil.UUIDFromSlot(slot, modifier.getId()), value.getDescriptionId(), 1 + 0.25 * bonus, AttributeModifier.Operation.MULTIPLY_BASE));
                bonus -= 1;
            } else break;
        }
    }
}
