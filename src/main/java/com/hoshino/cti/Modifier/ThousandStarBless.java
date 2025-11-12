package com.hoshino.cti.Modifier;

import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class ThousandStarBless extends Modifier implements TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP);
    }

    private static final String[] PLAYER_LEAVE_MESSAGE = {
            "香草大舜--落星 无尽之锭",
            "亚人测评:10分--亚人",
            "感觉不如勇者之章--摇光",
            "要玩一辈子的CTI--七曜",
            "你知道传说中的材料~血神锭吗--舜",
            "“穿过黑暗之门！”--浊心斯卡蒂",
            "不要石锅炖蜜蜂!--网友蜜顺峰",
            "虽然你都走到这里了，应该已经养成了看jei和任务的好习惯，但是不看jei和任务的通通达斯--Worldie",
            "嘻嘻，mek什么的我要一击打倒呀！--零逝",
            "复活吧，我的皇金--不会ae的我打算手搓无尽",
            "哦咩爹多~(鼓掌) --温泉",
            "小刘都觉得强--小刘",
            "当你打完CTI的时候，你就会知道我们还会再相遇--落秋枫"
    };


    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player==null)return;
        int[] originColor=new int[]{0xfd0004,0x0202fd,0x00fd26,0xfdfd00};
        String origin="漫长的旅途...终于迎来一个句号，手握此工具，无限之力贯彻天地古今，获得伤害增幅";
        long count= ModifierManager.INSTANCE.getAllValues().count();
        var one= DynamicComponentUtil.scrollColorfulText.getColorfulText(origin,":"+count * 100f+"%",originColor,20,20,false);

        int[] for_member_color=new int[]{0xffaaff,0x97ecff};
        String for_member="CTI不知不觉也1周年了,从最初的残破不堪,内容散乱,到现在的井井有条,离不开组内成员的每一个人,虽然总有分散的一天,但是我会永远记住每个人的.将来我也会有一天投奔生活,因此我希望有人能将这股力量传承下去";

        int[] for_player_color=new int[]{0xecd0ff,0xc8ffc5};
        String for_player="你们或许是从各种途径听说了CTI,或许是刚玩的,或许是多周目玩家,亦或许是从第一个版本支持到现在的老玩家,但是既然能玩到这里,能做出这个强化并且看到这里,我就会由衷的赞叹你的韧性,CTI一年以来听到过非常多的反馈,能成就今天自然少不了你们的一份功力,我再次由衷感谢各位喵,CTI的故事到这里或许就结束了,不过我希望你们能带着这份韧性继续走遍每一个整合包";

        String description="以下为群内玩家留言,感谢热爱这游戏的你们:";

        int tickCount=player.tickCount;
        int size=PLAYER_LEAVE_MESSAGE.length;
        int index= (tickCount/60) %size;
        String currentMessage=PLAYER_LEAVE_MESSAGE[index];


        var two=DynamicComponentUtil.BreathColorfulText.getColorfulText("对制作组其他成员",null,new int[]{0xffaaff},60,2000,false);
        var three=DynamicComponentUtil.scrollColorfulText.getColorfulText(for_member,null,for_member_color,40,30,false);

        var four=DynamicComponentUtil.BreathColorfulText.getColorfulText("对玩家说的话",null,new int[]{0x1fa5fd},60,2000,false);
        var five=DynamicComponentUtil.scrollColorfulText.getColorfulText(for_player,null,for_player_color,40,30,false);

        var six=DynamicComponentUtil.BreathColorfulText.getColorfulText(description,null,new int[]{0x1fa5fd},60,2000,false);
        var seven=DynamicComponentUtil.scrollColorfulText.getColorfulText(currentMessage,null,originColor,40,30,false);

        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        list.add(seven);
    }
}
