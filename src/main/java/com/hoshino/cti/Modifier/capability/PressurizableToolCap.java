package com.hoshino.cti.Modifier.capability;

import com.hoshino.cti.Cti;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.pressure.IPressurizableItem;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import me.desht.pneumaticcraft.common.capabilities.AirHandlerItemStack;
import me.desht.pneumaticcraft.common.core.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class PressurizableToolCap implements ToolCapabilityProvider.IToolCapabilityProvider, IPressurizableItem {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IPressurizableItem> capOptional;
    public final float maxPressure;
    public final LazyOptional<IAirHandlerItem> airHandlerItemLazyOptional;
    public static final ResourceLocation BASE_VOLUME_KEY = new ResourceLocation(Cti.MOD_ID, "base_volume");
    public static final ResourceLocation MAX_PRESSURE_KEY = new ResourceLocation(Cti.MOD_ID, "max_pressure");
    public static final ResourceLocation AIR_KEY = new ResourceLocation(Cti.MOD_ID, "air");

    public final AirHandlerItemStack airHandlerItemStack;


    public PressurizableToolCap(ItemStack stack, Supplier<? extends IToolStackView> tool) {
        this.tool = tool;
        this.capOptional = LazyOptional.of(() -> this);
        this.maxPressure = tool.get().getVolatileData().getInt(MAX_PRESSURE_KEY);
        this.airHandlerItemStack = new AirHandlerItemStack(new ItemStack(ModItems.REINFORCED_AIR_CANISTER.get()), this.maxPressure) {
            @Override
            public int getAir() {
                return tool.get().getPersistentData().getInt(AIR_KEY);
            }

            @Override
            public void addAir(int amount) {
                if (tool.get().getPersistentData().contains(AIR_KEY, 3)) {
                    tool.get().getPersistentData().putInt(AIR_KEY, tool.get().getPersistentData().getInt(AIR_KEY) + amount);
                } else {
                    tool.get().getPersistentData().putInt(AIR_KEY, amount);
                }
            }

            @Override
            public int getBaseVolume() {
                return tool.get().getVolatileData().getInt(BASE_VOLUME_KEY);
            }

            @Override
            public float getPressure() {
                return (float) this.getAir() / this.getBaseVolume();
            }

            @Override
            public void setBaseVolume(int newBaseVolume) {
            }

            @Override
            public int getVolume() {
                return this.getBaseVolume();
            }

            @Override
            public float maxPressure() {
                return tool.get().getVolatileData().getFloat(MAX_PRESSURE_KEY);
            }
        };
        this.airHandlerItemLazyOptional = LazyOptional.of(() -> this.airHandlerItemStack);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        if (tool.get().getVolatileData().getInt(BASE_VOLUME_KEY) > 0) {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY.orEmpty(capability, airHandlerItemLazyOptional);
        }
        return LazyOptional.empty();
    }

    @Override
    public int getBaseVolume() {
        return tool.get().getVolatileData().getInt(BASE_VOLUME_KEY);
    }

    @Override
    public int getVolumeUpgrades(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getAir(ItemStack itemStack) {
        return tool.get().getPersistentData().getInt(AIR_KEY);
    }

    @Override
    public int getEffectiveVolume(ItemStack stack) {
        return getBaseVolume();
    }

    public static float getPressure(IToolStackView tool) {
        return (float) tool.getPersistentData().getInt(AIR_KEY) / tool.getVolatileData().getInt(BASE_VOLUME_KEY);
    }

    public static int getAir(IToolStackView tool) {
        return tool.getPersistentData().getInt(AIR_KEY);
    }

    public static void addAir(IToolStackView tool, int amount) {
        if (tool.getPersistentData().contains(AIR_KEY, 3)) {
            tool.getPersistentData().putInt(AIR_KEY, tool.getPersistentData().getInt(AIR_KEY) + amount);
            if (tool.getPersistentData().getInt(AIR_KEY) < 0) {
                tool.getPersistentData().putInt(AIR_KEY, 0);
            }
        } else {
            tool.getPersistentData().putInt(AIR_KEY, amount);
        }
    }

    public static int getBaseVolume(IToolStackView tool) {
        return tool.getVolatileData().getInt(BASE_VOLUME_KEY);
    }

    public static float getMaxPressure(IToolStackView tool) {
        return tool.getVolatileData().getInt(MAX_PRESSURE_KEY);
    }
}
