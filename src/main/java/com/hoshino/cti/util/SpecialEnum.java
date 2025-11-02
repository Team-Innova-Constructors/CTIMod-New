package com.hoshino.cti.util;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public enum SpecialEnum {
    KillGlacier(getResourceLocation( "progress_glacier")),
    KillHydra(getResourceLocation( "progress_hydra")),
    KillUrGhast(getResourceLocation( "progress_ur_ghast"));
    @Getter
    private final ResourceLocation resourceLocation;
    SpecialEnum(ResourceLocation resourceLocation){
        this.resourceLocation=resourceLocation;
    }
    private static ResourceLocation getResourceLocation(String path){
        return new ResourceLocation("twilightforest",path);
    }
}
