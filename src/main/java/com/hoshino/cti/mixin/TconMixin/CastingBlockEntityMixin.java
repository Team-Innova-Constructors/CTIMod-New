package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.api.interfaces.IConditionalSpeedCastingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.shared.block.entity.TableBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.inventory.CastingContainerWrapper;
import slimeknights.tconstruct.smeltery.block.entity.tank.CastingFluidHandler;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = CastingBlockEntity.class,remap = false)
public abstract class CastingBlockEntityMixin extends TableBlockEntity implements WorldlyContainer {
    @Shadow private int coolingTime;

    @Shadow private int timer;

    @Shadow private ICastingRecipe currentRecipe;

    @Shadow @Final private CastingFluidHandler tank;

    @Shadow @Final private CastingContainerWrapper castingInventory;

    public CastingBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, Component name, int inventorySize) {
        super(tileEntityTypeIn, pos, state, name, inventorySize);
    }

    @Shadow @Nullable protected abstract ICastingRecipe findCastingRecipe();

    @Shadow private ResourceLocation recipeName;

    @Shadow private boolean lastRedstone;

    @Shadow public abstract void setItem(int slot, ItemStack stack);

    @Shadow @Final public static int OUTPUT;

    @Shadow @Final public static int INPUT;

    @Shadow protected abstract void updateAnalogSignal();

    @Shadow public abstract void reset();

    @Inject(method = "lambda$loadRecipe$3",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/recipe/casting/ICastingRecipe;getCoolingTime(Lslimeknights/tconstruct/library/recipe/casting/ICastingContainer;)I",shift = At.Shift.AFTER))
    public void modifyCoolingTime(FluidStack fluid, ICastingRecipe recipe, CallbackInfo ci){
        if ((CastingBlockEntity)(Object) this instanceof IConditionalSpeedCastingBlockEntity entity){
            coolingTime =Math.max(entity.modifyTotalCoolingTime(fluid,recipe,coolingTime),0);
        }
    }

    @ModifyArg(method = "onContentsChanged",at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I"),index = 1)
    public int modifyCoolingTime(int a){
        if ((CastingBlockEntity)(Object) this instanceof IConditionalSpeedCastingBlockEntity entity){
            return entity.modifyTotalCoolingTime(tank.getFluid(),currentRecipe,a);
        }
        return a;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void serverTick(Level level, BlockPos pos){
        IConditionalSpeedCastingBlockEntity icscb = null;
        if ((CastingBlockEntity)(Object) this instanceof IConditionalSpeedCastingBlockEntity entity){
            icscb = entity;
            entity.onServerTickStart();
        }
        if (currentRecipe == null) {
            return;
        }
        FluidStack currentFluid = tank.getFluid();
        if (coolingTime >= 0) {
            timer++;
            if (icscb!=null) timer+=icscb.getBoost(coolingTime);
            if (timer >= coolingTime) {
                if (!currentRecipe.matches(castingInventory, level)) {
                    currentRecipe = findCastingRecipe();
                    this.recipeName = null;
                    if (currentRecipe == null || currentRecipe.getFluidAmount(castingInventory) > currentFluid.getAmount()) {
                        timer = 0;
                        this.updateAnalogSignal();
                        if (icscb!=null) icscb.onServerTickEnd();
                        return;
                    }
                }
                ItemStack output = currentRecipe.assemble(castingInventory);
                if (currentRecipe.switchSlots() != this.lastRedstone) {
                    if (!currentRecipe.isConsumed()) {
                        this.setItem(OUTPUT, getItem(INPUT));
                    }
                    setItem(INPUT, output);
                    level.playSound(null, this.getBlockPos(), Sounds.CASTING_CLICKS.getSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    if (currentRecipe.isConsumed()) {
                        setItem(INPUT, ItemStack.EMPTY);
                    }
                    if (icscb!=null&&getBlockPos()!=null){
                        var blockPos = getBlockPos();
                        var sides = icscb.ejectSides();
                        AtomicBoolean inserted = new AtomicBoolean(false);
                        if (sides.isEmpty()) setItem(OUTPUT, output);
                        else for (Direction side : sides) {
                            var receiver = level.getBlockEntity(blockPos.relative(side));
                            if (receiver!=null) receiver.getCapability(ForgeCapabilities.ITEM_HANDLER,side.getOpposite()).ifPresent(handler->{
                                for (int i = 0; i < handler.getSlots(); i++) {
                                    if (handler.insertItem(i,output,true).isEmpty()){
                                        handler.insertItem(i,output,false);
                                        inserted.set(true);
                                        break;
                                    }
                                }
                            });
                            if (inserted.get()) break;
                        }
                        if (!inserted.get()) setItem(OUTPUT, output);
                    }
                    else setItem(OUTPUT, output);
                }
                level.playSound(null, pos, Sounds.CASTING_COOLS.getSound(), SoundSource.BLOCKS, 0.5f, 4f);
                if (icscb!=null) icscb.afterCraft();
                this.reset();
            } else {
                updateAnalogSignal();
            }
        }
        if (icscb!=null) icscb.onServerTickEnd();
    }
}
