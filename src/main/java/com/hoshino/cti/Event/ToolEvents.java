package com.hoshino.cti.Event;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Items.SlimeCanItem;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.library.modifier.hooks.OnDeathModifierHook;
import com.hoshino.cti.library.modifier.hooks.OnHoldingPreventDeathHook;
import com.hoshino.cti.library.modifier.hooks.SlotStackModifierHook;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.EquipmentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class ToolEvents {
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event){
        Player player = event.getEntity();
        if (player!=null&&player.level.isClientSide) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof IModifiable) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                LeftClickModifierHook.handleLeftClick(stack,player,slot);
            }
        }
    }
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        if (player!=null) {
            BlockState state = player.level.getBlockState(pos);
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof IModifiable) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                LeftClickModifierHook.handleLeftClickBlock(stack,player,slot,state,pos);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void EffectApply(MobEffectEvent.Applicable event){
        if (event.getEntity()!=null) {
            for (EquipmentSlot slot : EquipmentUtil.ALL) {
                if (event.getEntity().getItemBySlot(slot).getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(event.getEntity().getItemBySlot(slot));
                    boolean notApplicable = event.getResult()== Event.Result.DENY;
                    for (ModifierEntry entry:tool.getModifierList()){
                        notApplicable = entry.getHook(CtiModifierHook.EFFECT_APPLICABLE).isNotApplicable(tool,entry,slot,event.getEffectInstance(),notApplicable);
                        if (notApplicable){
                            event.setResult(Event.Result.DENY);
                            break;
                        }
                    }
                }
            }
        }
    }
    public static boolean shouldPrevent(DamageSource source, boolean canIgnore){
        if(source.isBypassInvul()){
            return canIgnore;
        }
        return true;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void ToolHoldingDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        DamageSource source = event.getSource();
        EquipmentContext context = new EquipmentContext(livingEntity);
        if (context.hasModifiableArmor()){
            for (EquipmentSlot slotType : EquipmentSlot.values()) {
                IToolStackView toolStack = context.getToolInSlot(slotType);
                if (toolStack != null && !toolStack.isBroken()) {
                    boolean canIgnore= OnHoldingPreventDeathHook.canIgnorePassInvul(CtiModifierHook.PREVENT_DEATH,context);
                    float HealthRemain = OnHoldingPreventDeathHook.onHoldingPreventDeath(CtiModifierHook.PREVENT_DEATH,context,source,event.getEntity());
                    if(shouldPrevent(source,canIgnore)&&HealthRemain>0){
                        event.setCanceled(true);
                        event.getEntity().setHealth(HealthRemain);
                        event.getEntity().sendSystemMessage(Component.translatable("etshtinker.message.death_prevent").withStyle(ChatFormatting.AQUA));
                        return;
                    }
                    if(livingEntity.isDeadOrDying()){
                        OnDeathModifierHook.handleDeath(CtiModifierHook.ON_DEATH,context,source,livingEntity);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onStackedItemOnOther(ItemStackedOnOtherEvent event){
        if(SlotStackModifierHook.handleSlotStackOnOther(event.getCarriedItem(),event.getSlot(),event.getClickAction(),event.getPlayer())){
            event.setCanceled(true);
            SlotStackModifierHook.handleSlotStackOnMe(event.getStackedOnItem(),event.getCarriedItem(),event.getSlot(),event.getClickAction(),event.getPlayer(),event.getCarriedSlotAccess());
        } else if (SlotStackModifierHook.handleSlotStackOnMe(event.getStackedOnItem(),event.getCarriedItem(),event.getSlot(),event.getClickAction(),event.getPlayer(),event.getCarriedSlotAccess())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<ItemStack> event){
        var stack = event.getObject();
        if (stack.getItem()== CtiItem.SLIME_CAN.get()){
            event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap== CuriosCapability.ITEM){
                        return LazyOptional.of(()-> new ICurio(){
                            @Override
                            public ItemStack getStack() {
                                return stack;
                            }
                            @Override
                            public void curioTick(SlotContext slotContext) {
                                SlimeCanItem.tick(slotContext,stack);
                            }
                        }).cast();
                    }
                    return LazyOptional.empty();
                }

                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
                    return getCapability(cap,null);
                }
            });
        }
    }


}
