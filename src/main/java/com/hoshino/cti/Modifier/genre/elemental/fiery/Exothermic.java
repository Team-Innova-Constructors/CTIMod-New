package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Cti;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelLookup;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Exothermic extends BasicBurntModifier implements LeftClickModifierHook {
    public static final ResourceLocation KEY_TEMP = Cti.getResource("exothermic_temp");
    public static final ResourceLocation KEY_TICKS = Cti.getResource("exothermic_ticks");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
        builder.addModule(ToolTankHelper.TANK_HANDLER);
        builder.addModule(StatBoostModule.add(ToolTankHelper.CAPACITY_STAT).eachLevel(FluidType.BUCKET_VOLUME*10));
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.8){
            if (!tool.getPersistentData().contains(KEY_TICKS,Tag.TAG_INT))
                convertFuel(tool,player);
            if (player.hasEffect(CtiEffects.OVERHEAT.get())&&consumeFuel(tool,player)>0){
                EntityTickerManager.getInstancePlayerSpecific(target,player.getUUID()).addTickerSimple(new EntityTickerInstance(CtiEntityTickers.FIERY.get(), 0,600));
            }
        }
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        consumeFuel(tool,holder instanceof Player player?player:null);
    }

    public static int consumeFuel(IToolStackView tool, @Nullable Player holder){
        var toolData = tool.getPersistentData();
        if (!toolData.contains(KEY_TICKS,Tag.TAG_INT)) return 0;
        int toConsume = holder!=null&&holder.hasEffect(CtiEffects.OVERHEAT.get())?2:1;
        var existing = toolData.getInt(KEY_TICKS);
        toConsume = Math.min(existing,toConsume);
        toolData.putInt(KEY_TICKS,existing-toConsume);
        if (existing-toConsume<=0){
            toolData.remove(KEY_TEMP);
            toolData.remove(KEY_TICKS);
        }
        return toConsume;
    }
    public static void convertFuel(IToolStackView tool, @Nullable Player holder){
        var toolData = tool.getPersistentData();
        if (!ToolTankHelper.TANK_HELPER.getFluid(tool).isEmpty()&&!toolData.contains(KEY_TICKS, Tag.TAG_INT)){
            var fluidStack = ToolTankHelper.TANK_HELPER.getFluid(tool);
            var fluid = fluidStack.getFluid();
            var fuel = MeltingFuelLookup.findFuel(fluid);
            if (fuel!=null){
                var temp = fuel.getTemperature();
                fluidStack.shrink(fuel.getAmount(fluid));
                toolData.putInt(KEY_TEMP,temp);
                toolData.putInt(KEY_TICKS,fuel.getDuration());
                ToolTankHelper.TANK_HELPER.setFluid(tool,fluidStack);
            }
        }
    }
    public static int getFuelTemp(IToolStackView tool,@Nullable Player player){
        int temp = tool.getPersistentData().getInt(KEY_TEMP);
        if (player!=null&&player.hasEffect(CtiEffects.OVERHEAT.get()))
            temp*=2;
        return temp;
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.burnt","info.cti.true_melee");
    }
}
