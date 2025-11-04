package com.hoshino.cti.Modifier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.EnigmaticEnchantmentHelper;
import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.CurseUtil;
import com.hoshino.cti.util.SearchTools;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class MandarinDuck extends Modifier implements ToolStatsModifierHook, TooltipModifierHook, ModifyDamageModifierHook {
    public static final ResourceLocation LOST_HEALTH = Cti.getResource("health_lost");
    public static final ResourceLocation LOST_HEALTH_TIME = Cti.getResource("health_lost_time");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.TOOLTIP, ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int m = context.getModifier(CtiModifiers.THE_PAST_STATIC_MODIFIER.getId()).getLevel() > 0 ? 4 : 2;
        float value = modifier.getLevel() * m;
        ToolStats.ARMOR.add(builder, value);
        ToolStats.ARMOR_TOUGHNESS.add(builder, value);
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.literal("你已成功触发彩蛋").withStyle(style -> style.withColor(0x350234)));
        list.add(Component.literal("吕布记忆中最鲜活的，不是权谋与厮杀\n而是破庙中与董卓赤诚相对的温暖，是共享一毯的私语，是那张喂饭时对他展露的笑颜……然而\n肩头旧伤的刺痛，总能将这一切甜蜜回忆瞬间撕裂。那夜他涕泪交加，愤恨与眷恋撕扯着他的灵魂\n自那时起，爱或许已死，但一丝复杂的“情”却扎根心底\n所以当他厉声质问“你有何话可说”时，泪水会不受控地决堤；而董卓临终射向他的那支箭矢，何尝不是另一种绝望的“情”的回应？当吕布最终蜷缩于神像之后，多年前火堆旁那个带着泪水的相拥，便是这乱世中，他们能给彼此最极致、也最悲哀的爱的证明了。此作的精髓，远非浅薄的香艳肥臀，而在于对这段扭曲又深情的宿命进行了一场盛大而凄美的收束，将历史洪流中的一对枭雄，重塑成了令人扼腕的苦命鸳鸯。").withStyle(style -> style.withColor(0xCAFDCC)));
        list.add(Component.literal("收到的来自生物伤害额外降低35%,你自己造成的伤害增加35%").withStyle(style -> style.withColor(0x350234)));
        list.add(Component.literal("彩蛋补给品已发放至背包").withStyle(style -> style.withColor(0xCAFDCC)));
    }


    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if (!(equipmentContext.getEntity() instanceof Player player)) return v;
        if(!SuperpositionHandler.isTheCursedOne(player))return v;
        var nbt = iToolStackView.getPersistentData();
        float amount = nbt.getFloat(LOST_HEALTH);
        int time = nbt.getInt(LOST_HEALTH_TIME);
        var curseNBT=CurseUtil.getCurseCurioData(player);
        if(curseNBT==null)return v;
        boolean activated=curseNBT.getBoolean("dongzhuo");
        if (b&&!activated) {
            if (amount < 350) {
                nbt.putFloat(LOST_HEALTH, amount + v);
            }
            if (time < 234) {
                nbt.putInt(LOST_HEALTH_TIME, time + 1);
            }
        }
        if (activated) {
            return v * 0.65f;
        } else if (amount >= 350 && time >= 234) {
            curseNBT.putBoolean("dongzhuo", true);
            nbt.remove(LOST_HEALTH);
            nbt.remove(LOST_HEALTH_TIME);
            runEgg(player);
            return v;
        } else return v;
    }

    private void giveItem(Player player, Item item, int count) {
        if (player.level.isClientSide) return;
        ItemStack stack = new ItemStack(item, count);
        player.getInventory().add(stack);
    }

    private static final String[] NAME_LIST = {
            "lightmanscurrency:coin_netherite",
            "lightmanscurrency:coin_diamond",
            "lightmanscurrency:coin_gold",
            "lightmanscurrency:coin_iron",
            "lightmanscurrency:coin_copper",
    };

    private void runEgg(Player player) {
        var itemList = SearchTools.findItemList(NAME_LIST);
        int[] count = {3, 5, 2, 3, 4};
        for (int i = 0; i < itemList.length; i++) {
            giveItem(player, itemList[i], count[i]);
        }
    }
}
