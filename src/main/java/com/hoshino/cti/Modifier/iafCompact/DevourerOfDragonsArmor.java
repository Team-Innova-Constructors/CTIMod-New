package com.hoshino.cti.Modifier.iafCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.hoshino.cti.Modifier.iafCompact.DevourerOfDragons.KEY_DISPLAY_BONUS;
import static com.hoshino.cti.Modifier.iafCompact.DevourerOfDragons.getBonus;

public class DevourerOfDragonsArmor extends EtSTBaseModifier implements AttributesModifierHook, ModifyDamageModifierHook {

    @Override
    public int getPriority() {
        return 16384;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.ATTRIBUTES,ModifierHooks.MODIFY_HURT);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.getEntity() instanceof EntityDragonBase) return amount*0.75f;
        return amount;
    }

    public static float getBonus(ServerPlayer player){
        var stats = player.getStats();
        float bonus = 0;
        bonus+= stats.getValue(Stats.ENTITY_KILLED.get(IafEntityRegistry.FIRE_DRAGON.get()));
        bonus+= stats.getValue(Stats.ENTITY_KILLED.get(IafEntityRegistry.ICE_DRAGON.get()));
        bonus+= stats.getValue(Stats.ENTITY_KILLED.get(IafEntityRegistry.LIGHTNING_DRAGON.get()));
        return Math.min(bonus*2,100);
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return super.getDisplayName().copy().append(" +"+String.format("%.0f",tool.getPersistentData().getFloat(KEY_DISPLAY_BONUS)));
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (holder instanceof ServerPlayer player&&level.getGameTime()%20==0){
            tool.getPersistentData().putFloat(KEY_DISPLAY_BONUS,getBonus(player));
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        UUID uuid = CommonUtil.UUIDFromSlot(slot,modifier.getId());
        List.of(Attributes.ARMOR,Attributes.ARMOR_TOUGHNESS,Attributes.MAX_HEALTH).forEach(attribute -> consumer.accept(attribute,new AttributeModifier(uuid,attribute.getDescriptionId(),tool.getPersistentData().getFloat(KEY_DISPLAY_BONUS), AttributeModifier.Operation.ADDITION)));
    }
}
