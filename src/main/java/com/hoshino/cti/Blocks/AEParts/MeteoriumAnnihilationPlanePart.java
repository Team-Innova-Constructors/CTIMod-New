package com.hoshino.cti.Blocks.AEParts;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
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
import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.hoshino.cti.register.CtiItem;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

import static com.hoshino.cti.util.CommonUtil.isAetherNight;

public class MeteoriumAnnihilationPlanePart extends AnnihilationPlanePart {
    private static final PlaneModels MODELS = new PlaneModels("part/meteorium_annihilation_plane", "part/meteorium_annihilation_plane_on");
    private final IActionSource actionSource = new MachineSource(this);
    private boolean rightPlace = false;
    public int process = 0;

    @PartModels
    public static List<IPartModel> getModels() {
        return MODELS.getModels();
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        BlockEntity host = this.getBlockEntity();
        if (host.hasLevel()) {
            int buildHeight = host.getLevel().getMaxBuildHeight();
            if (host.getBlockPos().getY() + 1 >= buildHeight && this.getSide() == Direction.UP) {
                rightPlace = true;
            }
        }
    }

    public MeteoriumAnnihilationPlanePart(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (isActive() && rightPlace) {
            process += ticksSinceLastCall;
            BlockEntity host = this.getBlockEntity();
            if (process > 1000&&host.getLevel()!=null&&isAetherNight(host.getLevel())) {
                Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:aetheric_meteorite_ore"))).ifPresent(item ->insertToGrid(AEItemKey.of(new ItemStack(item)), 1, Actionable.MODULATE));
                process = 0;
            }
            if (process > 2000) {
                insertToGrid(AEItemKey.of(new ItemStack(CtiItem.meteorite_ore.get())), 1, Actionable.MODULATE);
                process = 0;
            }
            return TickRateModulation.IDLE;
        }
        return TickRateModulation.SLEEP;
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
