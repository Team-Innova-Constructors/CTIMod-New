package com.hoshino.cti.Blocks.AEParts;

import appeng.api.behaviors.PickupSink;
import appeng.api.behaviors.PickupStrategy;
import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.StorageHelper;
import appeng.items.parts.PartModels;
import appeng.me.helpers.MachineSource;
import appeng.parts.automation.AnnihilationPlanePart;
import appeng.parts.automation.PlaneModels;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hoshino.cti.util.CommonUtil.isAetherNight;

public class QuantumizePlanePart extends AnnihilationPlanePart {
    private static final PlaneModels MODELS = new PlaneModels("part/quantumize_plane", "part/quantumize_plane_on");
    private final IActionSource actionSource = new MachineSource(this);
    private boolean rightPlace = false;
    public int process = 0;
    public static PickupStrategy quantumizeStrategy(ServerLevel level, BlockPos pos, Direction side, BlockEntity host, Map<Enchantment, Integer> enchantments) {
       return new PickupStrategy() {
           private boolean isAccepting = true;

           @Override
           public void reset() {
               isAccepting = false;
           }

           @Override
           public boolean canPickUpEntity(Entity entity) {
               return false;
           }

           @Override
           public boolean pickUpEntity(IEnergySource iEnergySource, PickupSink pickupSink, Entity entity) {
               return false;
           }

           @Override
           public Result tryPickup(IEnergySource iEnergySource, PickupSink pickupSink) {
               if (this.isAccepting) {
                   BlockState blockState = level.getBlockState(pos);
                   if (blockState.is(CtiBlock.quantum_meteorite_ore.get())){

                   }
               }
               return Result.CANT_PICKUP;
           }
       };
    }

    @PartModels
    public static List<IPartModel> getModels() {
        return MODELS.getModels();
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        BlockEntity host = this.getBlockEntity();
        if (host.hasLevel()) {
            int buildHeight = host.getLevel().getMinBuildHeight();
            if (host.getBlockPos().getY() - 1 <= buildHeight && this.getSide() == Direction.DOWN) {
                rightPlace = true;
            }
        }
    }

    public QuantumizePlanePart(IPartItem<?> partItem) {
        super(partItem);

    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (isActive() && rightPlace) {
            process += ticksSinceLastCall;
            BlockEntity host = this.getBlockEntity();
            if (process > 1000&&host.getLevel()!=null&&host.getLevel().dimension()== Level.END) {
                insertToGrid(AEItemKey.of(CtiItem.quantum_meteorite_ore.get()),1,Actionable.MODULATE);
            }
            return TickRateModulation.IDLE;
        }
        return super.tickingRequest(node,ticksSinceLastCall);
    }

    @Override
    protected List<PickupStrategy> getPickupStrategies() {
        if (this.pickupStrategies==null) {
            BlockEntity self = this.getHost().getBlockEntity();
            BlockPos pos = self.getBlockPos().relative(this.getSide());
            Direction side = this.getSide().getOpposite();
            this.pickupStrategies.add(quantumizeStrategy((ServerLevel) this.getLevel(),pos,side,self,this.getEnchantments()));
            super.getPickupStrategies();
        }
        return this.pickupStrategies;
    }

    public long insertToGrid(AEKey what, long amount, Actionable mode) {
        var grid = getMainNode().getGrid();
        if (grid == null) {
            return 0;
        }
        return StorageHelper.poweredInsert(grid.getEnergyService(), grid.getStorageService().getInventory(),
                what, amount, this.actionSource, mode);
    }

    public IPartModel getStaticModels() {
        return MODELS.getModel(this.isPowered(), this.isActive());
    }
}