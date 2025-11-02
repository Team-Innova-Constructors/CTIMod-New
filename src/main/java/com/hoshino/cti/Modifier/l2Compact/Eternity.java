package com.hoshino.cti.Modifier.l2Compact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class Eternity extends EtSTBaseModifier implements ToolDamageModifierHook {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_ETERNITY = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("key_eternity"));

    @Override
    public int getPriority() {
        return 10000;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(KEY_ETERNITY,false, TinkerTags.Items.MODIFIABLE));
        builder.addHook(this, ModifierHooks.TOOL_DAMAGE);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        if (event.getSource().isBypassInvul()) return;
        event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
            if (cap.get(KEY_ETERNITY,0)>0) event.setAmount(event.getAmount()/2);
        });
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return 0;
    }
}
