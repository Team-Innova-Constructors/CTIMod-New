package com.hoshino.cti.register;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CtiEntityData {
//    public static final EntityDataSerializer<Vec3> VEC3 = new EntityDataSerializer.ForValueType<>() {
//        @Override
//        public void write(FriendlyByteBuf pBuffer, Vec3 pValue) {
//            pBuffer.writeDouble(pValue.x());
//            pBuffer.writeDouble(pValue.y());
//            pBuffer.writeDouble(pValue.z());
//        }
//
//        @Override
//        public @NotNull Vec3 read(FriendlyByteBuf pBuffer) {
//            return new Vec3(pBuffer.readDouble(), pBuffer.readDouble(), pBuffer.readDouble());
//        }
//    };
//    static{
//        EntityDataSerializers.registerSerializer(VEC3);
//    }
//    public static void init(){}
}
