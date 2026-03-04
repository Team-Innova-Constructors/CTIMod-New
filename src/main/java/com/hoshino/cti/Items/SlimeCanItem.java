package com.hoshino.cti.Items;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.hoshino.cti.Cti;
import com.hoshino.cti.mixin.TconMixin.OverslimeModifierRecipeAccessor;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiTab;
import com.hoshino.cti.util.tinker.ModifiableToolDefinition;
import com.hoshino.cti.util.tinker.ModifiableToolDefinitionData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.adding.OverslimeModifierRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlimeCanItem extends ModifiableItem implements ICurioItem {
    public static final ModifiableToolDefinitionData SLIME_CAN_DATA = new ModifiableToolDefinitionData(){
        @Override
        public void registerHook(ModuleHookMap.Builder builder) {
            builder.addModule(new ToolTraitsModule(List.of(new ModifierEntry(TinkerModifiers.overslime.getId(),1),
                    new ModifierEntry(CtiModifiers.SUPER_OVERSLIME.getId(),1))));
            builder.addModule(ToolSlotsModule.builder().slots(SlotType.ABILITY,1).slots(SlotType.UPGRADE,3).build());
        }
    };
    public static final Map<Item,Integer> OVERSLIME_VALUES = new HashMap<>();
    public static void initOverslimeValues(Level level){
        level.getRecipeManager().getAllRecipesFor(TinkerRecipeTypes.TINKER_STATION.get()).stream().filter(recipe->recipe instanceof OverslimeModifierRecipe)
                .forEach(recipe->{
                    var osr = (OverslimeModifierRecipe)recipe;
                    osr.getDisplayItems(0).stream().filter(stack -> !stack.isEmpty()).map(ItemStack::getItem).forEach(item ->
                            OVERSLIME_VALUES.put(item,((OverslimeModifierRecipeAccessor)osr).getRestoreAmount()));
                });
    }
    public SlimeCanItem() {
        super(new Item.Properties().stacksTo(1).tab(CtiTab.MIXC), new ModifiableToolDefinition(Cti.getResource("slime_can"),SLIME_CAN_DATA));
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pAction==ClickAction.PRIMARY) return false;
        if (OVERSLIME_VALUES.isEmpty()) initOverslimeValues(pPlayer.level);
        if (OVERSLIME_VALUES.containsKey(pOther.getItem())){
            if (!pPlayer.level.isClientSide){
                var slimeball = pOther.getItem();
                var tool = ToolStack.from(pStack);
                var os = TinkerModifiers.overslime.get();
                var needed = tool.getStats().getInt(OverslimeModifier.OVERSLIME_STAT)-os.getShield(tool);
                var perBall = OVERSLIME_VALUES.get(slimeball);
                var calculateBalls = (tool.getModifierLevel(TinkerModifiers.overworked.get())>0)? perBall*=2:perBall;
                if (needed>=calculateBalls){
                    var count = Math.min(pOther.getCount(),needed/calculateBalls);
                    pOther.shrink(count);
                    os.addOverslime(tool,new ModifierEntry(os.getId(),1),perBall*count);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pAction==ClickAction.PRIMARY) return false;
        if (pSlot.getItem().getItem() instanceof IModifiable){
            if (!pPlayer.level.isClientSide) {
                var can = ToolStack.from(pStack);
                var tool = ToolStack.from(pSlot.getItem());
                var os = TinkerModifiers.overslime.get();
                var needed = tool.getStats().getInt(OverslimeModifier.OVERSLIME_STAT) - os.getShield(tool);
                var existing = tool.getModifierLevel(TinkerModifiers.overworked.getId()) > 0 ? os.getShield(can) / 2 : os.getShield(can);
                needed = Math.min(needed, existing);
                var entry = new ModifierEntry(os.getId(), 1);
                if (needed > 0) {
                    os.addOverslime(can, entry, -needed);
                    os.addOverslime(tool, entry, needed);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.item.cti.slime_can_1").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.item.cti.slime_can_2").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !SuperpositionHandler.hasCurio(slotContext.entity(), this);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
