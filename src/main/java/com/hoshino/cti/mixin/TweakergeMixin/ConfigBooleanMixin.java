package com.hoshino.cti.mixin.TweakergeMixin;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ConfigBoolean.class,remap = false)
public class ConfigBooleanMixin {
}
