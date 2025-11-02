package com.hoshino.cti.util;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.function.Consumer;

@FunctionalInterface
public interface ChangeBossHealth {
    void cti$changeMaxHealthAttributeInstance(Consumer<AttributeInstance> multimapConsumer);
}
