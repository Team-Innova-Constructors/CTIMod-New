package com.hoshino.cti.Blocks;

import com.hoshino.cti.api.interfaces.IConfigurableFaucetBlock;
import com.hoshino.cti.util.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.smeltery.block.FaucetBlock;

import java.util.List;
import java.util.function.Supplier;

public class ConfigurableFaucetBlock extends FaucetBlock implements IConfigurableFaucetBlock {
    private final Supplier<Integer> transferConfig;
    private final Supplier<Integer> perTickConfig;
    private int extractFactor = 1;
    public ConfigurableFaucetBlock(Properties properties, Supplier<Integer> transferConfig, Supplier<Integer> perTickConfig) {
        super(properties);
        this.transferConfig = transferConfig;
        this.perTickConfig = perTickConfig;
    }
    public ConfigurableFaucetBlock(Properties properties,Supplier<Integer> perTickConfig,int extractFactor) {
        this(properties,perTickConfig,perTickConfig);
        this.extractFactor = extractFactor;
    }
    public ConfigurableFaucetBlock(Properties properties,Supplier<Integer> perTickConfig) {
        this(properties,perTickConfig,perTickConfig);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("tooltip.cti.faucet_transfer").append(MathUtil.getUnitForFluid(perTickConfig.get())+"/t").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getMaxTransfer() {
        return transferConfig.get()*extractFactor;
    }

    @Override
    public int getTransferPerTick() {
        return perTickConfig.get();
    }
}
