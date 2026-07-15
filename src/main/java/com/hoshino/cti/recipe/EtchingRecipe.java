package com.hoshino.cti.recipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.json.IntRange;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EtchingRecipe extends ModifierRecipe {
    public static final RecordLoadable<EtchingRecipe> LOADER = RecordLoadable.create(ContextKey.ID.requiredField(),
            INPUTS_FIELD,
            TOOLS_FIELD,
            MAX_TOOL_SIZE_FIELD,
            RESULT_FIELD,
            LEVEL_FIELD,
            SLOTS_FIELD,
            ALLOW_CRYSTAL_FIELD,
            CHECK_TRAIT_LEVEL_FIELD,
            EtchingRecipe::new);

    public EtchingRecipe(ResourceLocation id, List<SizedIngredient> inputs, Ingredient toolRequirement, int maxToolSize, ModifierId result, IntRange level, @Nullable SlotType.SlotCount slots, boolean allowCrystal, boolean checkTraitLevel) {
        super(id, inputs, toolRequirement, maxToolSize, result, level, slots, allowCrystal, checkTraitLevel);
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, Level world) {
        var stack = inv.getTinkerableStack();
        if (stack.getItem() instanceof IModifiable){
            var tool = ToolStack.from(stack);
            var def = tool.getDefinition();
            for (int i = 0; i < inv.getInputCount(); i++) {
                var input = inv.getInput(i);
                if (input.getItem() instanceof IToolPart part){
                    AtomicBoolean b = new AtomicBoolean(false);
                    def.getHook(ToolHooks.TOOL_PARTS).getParts(def).forEach(part1 ->{
                        if (part1 == part) b.set(true);
                    });
                    if (b.get()) return super.matches(inv, world);
                }
            }
        }
        return false;
    }

    @Override
    public RecipeResult<ItemStack> getValidatedResult(ITinkerStationContainer inv) {
        ToolStack tool = inv.getTinkerable();

        // common errors
        Component commonError = validatePrerequisites(tool);
        if (commonError != null) {
            return RecipeResult.failure(commonError);
        }

        // consume slots
        tool = tool.copy();
        ModDataNBT persistentData = tool.getPersistentData();
        SlotType.SlotCount slots = getSlots();
        if (slots != null) {
            persistentData.addSlots(slots.type(), -slots.count());
        }

        // add modifier
        var modifierId = result.getId();
        for (int i = 0; i < inv.getInputCount(); i++) {
            var input = inv.getInput(i);
            if (input.getItem() instanceof IToolPart) {
                tool.getPersistentData().put(modifierId, input.serializeNBT());
                break;
            }
        }
        tool.addModifier(modifierId, 1);

        // ensure no modifier problems
        Component toolValidation = tool.tryValidate();
        if (toolValidation != null) {
            return RecipeResult.failure(toolValidation);
        }

        return RecipeResult.success(tool.createStack(Math.min(inv.getTinkerableSize(), shrinkToolSlotBy())));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ctiRecipes.ETCHING_RECIPE_SERIALIZER.get();
    }
}
