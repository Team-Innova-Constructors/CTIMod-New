package com.hoshino.cti.Modifier.Contributors;

import com.hoshino.cti.util.CtiTagkey;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class Mz extends Modifier implements BlockBreakModifierHook , MeleeDamageModifierHook , TooltipModifierHook , ProcessLootModifierHook , InventoryTickModifierHook {
    public static final ResourceLocation MZBreak=new ResourceLocation("mz_break");
    public static final ResourceLocation CurrentMZ=new ResourceLocation("current_mz");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.BLOCK_BREAK,ModifierHooks.MELEE_DAMAGE,ModifierHooks.TOOLTIP,ModifierHooks.PROCESS_LOOT,ModifierHooks.INVENTORY_TICK);
    }
    public static int getMaterialCount(IToolStackView view){
        var materialVariant=view.getMaterials();
        int count=0;
        var miziId = new MaterialId("cti:lsmz");
        for (MaterialVariant variant : materialVariant.getList()) {
            if (variant.getId().equals(miziId)) {
                count++;
            }
        }
        return count;
    }

    public static int miZiPoint(IToolStackView view){
        int amount= view.getPersistentData().getInt(MZBreak);
        var level=getMaterialCount(view);
        if(amount==0)return 1;
        int count=0;
        int n=Math.abs(amount);
        while (n>0){
            count++;
            n/=10;
        }
        return count * level;
    }

    @Override
    public void afterBlockBreak(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolHarvestContext toolHarvestContext) {
        int amount=iToolStackView.getPersistentData().getInt(MZBreak);
        if(toolHarvestContext.getPlayer() instanceof FakePlayer)return;
        if(iToolStackView.hasTag(TinkerTags.Items.HARVEST)){
            iToolStackView.getPersistentData().putInt(MZBreak,amount+1);
        }
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float v, float v1) {
        int miziPoint=miZiPoint(iToolStackView);
        return v1 * Math.round(Math.sqrt(miziPoint));
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int amount=iToolStackView.getPersistentData().getInt(MZBreak);
        list.add(Component.literal("当前总挖掘方块数"+amount));
        list.add(Component.literal("当前迷子点数"+miZiPoint(iToolStackView)));
        list.add(Component.literal("当前剩余免死点数"+iToolStackView.getPersistentData().getInt(CurrentMZ)));
    }

    @Override
    public void processLoot(IToolStackView iToolStackView, ModifierEntry modifierEntry, List<ItemStack> list, LootContext lootContext) {
        if (!iToolStackView.getStats().hasStat(ToolStats.HARVEST_TIER)) {
            return;
        }
        var targetBlock = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);
        Vec3 origin=lootContext.getParamOrNull(LootContextParams.ORIGIN);
        if (targetBlock == null || targetBlock.isAir()) {
            return;
        }
        if(origin==null)return;
        if(targetBlock.getDestroySpeed(lootContext.getLevel(),new BlockPos(origin))<0){
            return;
        }
        boolean applyMultiplier = false;
        var block = targetBlock.getBlock();
        if (block instanceof CropBlock cropBlock) {
            applyMultiplier = cropBlock.isMaxAge(targetBlock);
        } else if (targetBlock.is(CtiTagkey.ORE)) {
            applyMultiplier = true;
        }
        if (applyMultiplier) {
            int mizi = miZiPoint(iToolStackView);
            if(mizi<1)return;
            if(mizi>3){
                mizi=3;
            }
            for (ItemStack stack : list) {
                if (stack.is(targetBlock.getBlock().asItem())) {
                    return;
                }
            }
            for(ItemStack stack:list){
                stack.setCount(mizi * stack.getCount());
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(!(livingEntity instanceof Player player))return;
        int mizi=miZiPoint(iToolStackView);
        if(player.level.getGameTime()%24000==0){
            iToolStackView.getPersistentData().putInt(CurrentMZ,mizi);
        }
    }
}
