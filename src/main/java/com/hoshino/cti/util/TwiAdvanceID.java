package com.hoshino.cti.util;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public enum TwiAdvanceID {
    castle(getResourceLocation( "progress_castle")),
    KillHydra(getResourceLocation( "progress_hydra")),
    KillNaga(getResourceLocation( "progress_naga")),
    KillGlacier(getResourceLocation( "progress_glacier")),
    KillUrGhast(getResourceLocation( "progress_ur_ghast"));
    @Getter
    private final ResourceLocation resourceLocation;
    TwiAdvanceID(ResourceLocation resourceLocation){
        this.resourceLocation=resourceLocation;
    }
    private static ResourceLocation getResourceLocation(String path){
        return new ResourceLocation("twilightforest",path);
    }
}
