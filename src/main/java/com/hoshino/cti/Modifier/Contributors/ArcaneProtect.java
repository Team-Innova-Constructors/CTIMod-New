package com.hoshino.cti.Modifier.Contributors;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.List;

public class ArcaneProtect extends ArmorModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    public static void setfakeInvurabletime(int time, Player player) {
        player.getPersistentData().putInt("fakeInvurabletime", time);
    }

    public static int getfakeInvurabletime(Player player) {
        return player.getPersistentData().getInt("fakeInvurabletime");
    }

    public ArcaneProtect() {
        MinecraftForge.EVENT_BUS.addListener(this::LivingMagicDamage);
    }

    private void LivingMagicDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player && GetModifierLevel.EquipHasModifierlevel(player, this.getId())) {
            if (event.getSource().isMagic()) {
                event.setAmount(1);
            }
            if (GetModifierLevel.getTotalArmorModifierlevel(player,this.getId())<=0) return;
            if (getfakeInvurabletime(player) == 60) {
                setfakeInvurabletime(0, player);
                player.setInvulnerable(true);
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isCorrectSlot) {
            if (entity instanceof Player player) {
                if (getfakeInvurabletime(player) < 60) {
                    setfakeInvurabletime(getfakeInvurabletime(player) + 1, player);
                } else if (getfakeInvurabletime(player) == 60 && !GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.NATIVED_STATIC_MODIFIER.getId())) {
                    entity.setInvulnerable(false);
                }
            }
        }
    }
}
