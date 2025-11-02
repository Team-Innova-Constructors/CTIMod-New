package com.hoshino.cti.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum CurseStage {
    wind(96000,
            Component.literal("风起云涌").withStyle(style -> style.withColor(0xd3fcff)),
            Component.literal("一阶段").withStyle(style -> style.withColor(0xd3fcff)),
            Component.literal("一些中立生物会主动开始攻击你,一定要小心他们,因为那可能已经不是他们了\n末地之门已经为你打开").withStyle(style -> style.withColor(0xd3fcff)),
            new ItemStack[]{
                    new ItemStack(Items.DIAMOND, 32)
            }
    ),
    fire(192000,
            Component.literal("烈火丛生").withStyle(style -> style.withColor(0xd3fcff)),
            Component.literal("二阶段").withStyle(style -> style.withColor(0xd3fcff)),
            Component.literal("你被火焰点燃时不再会自然熄灭,在一些没有水的维度一定要小心\n末地之门已经为你打开").withStyle(style -> style.withColor(0xd3fcff)),
            new ItemStack[]{
                    new ItemStack(Items.DIAMOND, 32)
            }
    ),
    water(288000,
            Component.literal("海蕴生机").withStyle(style -> style.withColor(0x2e77ff)),
            Component.literal("三阶段").withStyle(style -> style.withColor(0x2e77ff)),
            Component.literal("你将会正常受到窒息伤害,同时由于水元素的充盈,你受到的伤害自此翻倍\n海洋深处的某个漩涡在等着你").withStyle(style -> style.withColor(0x2e77ff)),
            new ItemStack[]{
                    new ItemStack(Items.DIAMOND, 32)
            }
    );
    public final long curseTime;
    public final Component title;
    public final Component subTitle;
    public final Component description;

    CurseStage(long curseTime, Component title, Component subTitle, Component description, ItemStack[] stackArray) {
        this.curseTime = curseTime;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
    }

}
