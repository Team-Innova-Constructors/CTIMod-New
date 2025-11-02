package com.hoshino.cti.content.environmentSystem;

import net.minecraft.world.entity.ai.attributes.Attribute;

//环境伤害的接口，实现了此类的伤害来源即视为环境伤害。
public interface IEnvironmentalSource {
    //是否来自群系，为true时不被环境抗性减免。
    boolean fromBiomes();
    //环境危害等级，仅在fromBiomes()为false时有意义，此时环境伤害的量会乘以危害等级与生物抗性等级的差。
    int getLevel();
    //对应抗性的Attribute，在fromBiomes()为false时决定此环境伤害会被哪种抗性减免。
    Attribute getResistAttribute();
}
