package com.hoshino.cti.mixin.PowahMixin;

import com.hoshino.cti.register.CtiItem;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorBlock;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.util.Ticker;

@Mixin(ReactorTile.class)
public abstract class ReactorTileMixin extends AbstractEnergyProvider<ReactorBlock> implements IInventoryHolder, ITankHolder {
    @Shadow(remap = false)
    @Final
    public Ticker solidCoolant;

    @Shadow(remap = false)
    public int solidCoolantTemp;

    @Shadow(remap = false)
    private int baseTemp;

    @Shadow(remap = false)
    private int carbonTemp;

    @Shadow(remap = false)
    private int redstoneTemp;

    @Shadow(remap = false)
    @Final
    public Ticker temp;

    @Shadow(remap = false)
    @Final
    public Ticker carbon;

    @Shadow(remap = false) @Final public Ticker redstone;

    @Shadow(remap = false) @Final public Ticker fuel;

    public ReactorTileMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, Tier variant) {
        super(type, pos, state, variant);
    }
    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true,remap = false)
    private void onCanInsert(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot == 3) {
            boolean customCondition = stack.is(Items.REDSTONE) || stack.is(Items.REDSTONE_BLOCK)||stack.is(CtiItem.refined_stone.get());
            cir.setReturnValue(customCondition);
        }
    }


    /**
     * @author firefly
     * @reason 重写逆天温度逻辑
     */
    @Overwrite(remap = false)
    private boolean processTemperature(Level world, boolean generating) {
        boolean flag = false;
        if (this.solidCoolant.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(4);
            if (!stack.isEmpty()) {
                Pair<Integer, Integer> coolant = PowahAPI.getSolidCoolant(stack.getItem());
                int size = coolant.getLeft();
                int temp = coolant.getRight();
                if (size > 0) {
                    this.solidCoolant.setAll(size);
                    this.solidCoolantTemp = temp;
                    stack.shrink(1);
                    flag = true;
                }
            }
        }
        if (!this.solidCoolant.isEmpty()) {
            if (!this.tank.isEmpty() && generating && this.ticks % 40 == 0) {
                this.solidCoolant.back();
                if (this.solidCoolant.isEmpty()) this.solidCoolant.setMax(0.0F);
                flag = true;
            }
        } else {
            this.solidCoolantTemp = 0;
        }

        double targetTemp;
        double potentialHeat = this.baseTemp + this.carbonTemp + this.redstoneTemp;

        double coolantBase;
        if (!this.tank.isEmpty()) {
            double fluidTemp = PowahAPI.getCoolant(this.tank.getFluid().getFluid());
            double solidTemp = this.solidCoolant.isEmpty() ? 25.0 : (double) this.solidCoolantTemp;
            coolantBase = (fluidTemp + solidTemp) / 2.0;
        } else {
            double solidTemp = this.solidCoolant.isEmpty() ? 40.0 : (double) this.solidCoolantTemp;
            coolantBase = (40.0 + solidTemp) / 2.0;
        }

        double thermalInertia = 0.10;
        targetTemp = (coolantBase * (1.0 - thermalInertia)) + (potentialHeat * thermalInertia);

        targetTemp = Math.max(-120.0, targetTemp);
        if (this.ticks % 10 == 0) this.sync(5);
        double currentTemp = this.temp.getTicks();
        double step = 1.0;

        if (Math.abs(currentTemp - targetTemp) > 0.1) {
            if (currentTemp < targetTemp) {
                this.temp.setTicks(Math.min(targetTemp, currentTemp + step));
            } else {
                int coolingFrequency = this.tank.isEmpty() ? 5 : (this.solidCoolant.isEmpty() ? 3 : 1);
                if (this.ticks % coolingFrequency == 0) {
                    this.temp.setTicks(Math.max(targetTemp, currentTemp - step));
                }
            }
            flag = true;
        }

        return flag;
    }

    /**
     * @author firefly
     * @reason 改煤炭基础温度
     */
    @Overwrite(remap = false)
    private boolean processCarbon(Level world, boolean generating) {
        boolean flag = false;
        if (this.carbon.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(2);
            if (!stack.isEmpty()) {
                int carbon = FuelRegistry.get(stack);
                if (carbon > 0) {
                    this.carbon.setAll(carbon);
                    this.carbonTemp = 180;
                    stack.shrink(1);
                    flag = true;
                }
            }
        }
        if (!this.carbon.isEmpty()) {
            if (generating) {
                this.carbon.back();
                if (this.carbon.isEmpty()) {
                    this.carbon.setMax(0.0F);
                }
            }
        } else {
            this.carbonTemp = 0;
        }

        return flag;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public double calc() {
        double redStoneF = this.redstone.isEmpty() ? 1.0 : 1.4;
        double currentTemp = this.temp.getTicks();
        double equ = currentTemp + 120;
        if (equ < 0) equ = 0;
        return (equ / 3600.0) * 0.98 * redStoneF;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public double calcProduction() {
        double d = this.carbon.isEmpty() ? 1.6 : (double)1.0F;
        double d1 = this.redstone.isEmpty() ? 2.2 : (double)1.0F;
        return ((double)1.0F - this.calc()) * (this.fuel.getTicks() / (double)1000.0F) * (double)this.getGeneration() / d / d1;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private boolean processRedstone(Level world, boolean generating) {
        boolean flag = false;
        if (this.redstone.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(3);
            if (stack.getItem() == Items.REDSTONE) {
                this.redstone.setAll(18.0);
                flag = true;
            } else if (stack.getItem() == Items.REDSTONE_BLOCK) {
                this.redstone.setAll(162.0);
                flag = true;
            }else if(stack.getItem() == CtiItem.refined_stone.get()){
                this.redstone.setAll(400.0);
                flag = true;
            }
            if (flag) {
                this.redstoneTemp = 240;
                stack.shrink(1);
            }
        }
        if (!this.redstone.isEmpty()) {
            int consumptionTick = 200;
            if (generating && this.ticks % consumptionTick == 0) {
                this.redstone.back();
                if (this.redstone.isEmpty()) {
                    this.redstone.setMax(0.0);
                }
                flag = true;
            }
        } else {
            this.redstoneTemp = 0;
        }
        return flag;
    }
}
