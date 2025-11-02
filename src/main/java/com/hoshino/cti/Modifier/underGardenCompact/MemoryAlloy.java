package com.hoshino.cti.Modifier.underGardenCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.SlotStackModifierHook;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.List;

public class MemoryAlloy extends EtSTBaseModifier implements SlotStackModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.SLOT_STACK);
    }

    public static final ResourceLocation KEY_DAMAGE_BOOST = Cti.getResource("memory_damage");
    public static final ResourceLocation KEY_MEMORIZED_DAMAGE = Cti.getResource("memorized_damages");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(IToolStackView slotTool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        if (held.is(Items.BLUE_ICE) && slotTool.getPersistentData().getInt(KEY_MEMORIZED_DAMAGE)>0){
            if (player instanceof ServerPlayer){
                slotTool.getPersistentData().remove(KEY_MEMORIZED_DAMAGE);
                slotTool.getPersistentData().remove(KEY_DAMAGE_BOOST);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 1024;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return damage+tool.getPersistentData().getFloat(KEY_DAMAGE_BOOST)*0.5f;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (damageDealt<=10) return;
        if (context.getLivingTarget()==null) return;
        ModDataNBT nbt = tool.getPersistentData();
        int memorizedDamages = nbt.getInt(KEY_MEMORIZED_DAMAGE)+1;
        float memoryDamage = nbt.getFloat(KEY_DAMAGE_BOOST)*memorizedDamages;
        if (memorizedDamages>=10000*modifier.getLevel()) return;
        nbt.putFloat(KEY_DAMAGE_BOOST,Math.min(10000*modifier.getLevel(),memoryDamage+damageDealt)/memorizedDamages);
        nbt.putInt(KEY_MEMORIZED_DAMAGE,memorizedDamages);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        tooltip.add(this.getDisplayName().copy().append(" ").append(Component.translatable("cti.tooltip.modifier.damage_boost").append(": "+String.format("%.1f",tool.getPersistentData().getFloat(KEY_DAMAGE_BOOST)*0.5f))));
    }
}
