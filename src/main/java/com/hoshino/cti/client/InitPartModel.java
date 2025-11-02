package com.hoshino.cti.client;

import appeng.client.render.SimpleModelLoader;
import appeng.core.AppEng;
import appeng.parts.automation.PlaneModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.ibm.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class InitPartModel {
    private static BiConsumer<String, IGeometryLoader<?>> register = null;
    public static void init(BiConsumer<String, IGeometryLoader<?>> register) {
        InitPartModel.register = register;
        addPlaneModel("part/meteorium_annihilation_plane","part/meteorium_annihilation_plane");
        addPlaneModel("part/meteorium_annihilation_plane_on","part/meteorium_annihilation_plane_on");
        InitPartModel.register =null;
    }
    private static void addPlaneModel(String planeName, String frontTexture) {
        ResourceLocation frontTextureId = AppEng.makeId(frontTexture);
        ResourceLocation sidesTextureId = AppEng.makeId("part/plane_sides");
        ResourceLocation backTextureId = AppEng.makeId("part/transition_plane_back");
        addBuiltInModel(planeName, () -> {
            return new PlaneModel(frontTextureId, sidesTextureId, backTextureId);
        });
    }

    private static <T extends IUnbakedGeometry<T>> void addBuiltInModel(String id, Supplier<T> modelFactory) {
        register.accept(id, new SimpleModelLoader<>(modelFactory));
    }
}
