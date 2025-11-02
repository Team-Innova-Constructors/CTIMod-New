package com.hoshino.cti.netwrok;

import com.hoshino.cti.Cti;
import com.hoshino.cti.netwrok.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


public class CtiPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;
    static int id = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Cti.MOD_ID, "cti_message")).networkProtocolVersion(() -> "1").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE.messageBuilder(PIonizeValueSync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PIonizeValueSync::new).encoder(PIonizeValueSync::toByte).consumerMainThread(PIonizeValueSync::handle).add();
        INSTANCE.messageBuilder(PScorchValueSync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PScorchValueSync::new).encoder(PScorchValueSync::toByte).consumerMainThread(PScorchValueSync::handle).add();
        INSTANCE.messageBuilder(PFrozenValueSync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PFrozenValueSync::new).encoder(PFrozenValueSync::toByte).consumerMainThread(PFrozenValueSync::handle).add();
        INSTANCE.messageBuilder(PMachineEnergySync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PMachineEnergySync::new).encoder(PMachineEnergySync::toByte).consumerMainThread(PMachineEnergySync::handle).add();
        INSTANCE.messageBuilder(PMachineFluidSync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PMachineFluidSync::new).encoder(PMachineFluidSync::toByte).consumerMainThread(PMachineFluidSync::handle).add();
        INSTANCE.messageBuilder(PPressureValueSync.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PPressureValueSync::new).encoder(PPressureValueSync::toByte).consumerMainThread(PPressureValueSync::handle).add();
        INSTANCE.messageBuilder(PStellarBlade.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PStellarBlade::new).encoder(PStellarBlade::toByte).consumerMainThread(PStellarBlade::handle).add();
        INSTANCE.messageBuilder(PRailgunItemS2C.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PRailgunItemS2C::new).encoder(PRailgunItemS2C::toByte).consumerMainThread(PRailgunItemS2C::handle).add();
        INSTANCE.messageBuilder(PRailgunC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PRailgunC2S::new).encoder(PRailgunC2S::toByte).consumerMainThread(PRailgunC2S::handle).add();
        INSTANCE.messageBuilder(PAttackSelfC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PAttackSelfC2S::new).encoder(PAttackSelfC2S::toByte).consumerMainThread(PAttackSelfC2S::handle).add();
        INSTANCE.messageBuilder(PStarFuryC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PStarFuryC2S::new).encoder(PStarFuryC2S::toByte).consumerMainThread(PStarFuryC2S::handle).add();
        INSTANCE.messageBuilder(PLeftClickEmptyC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PLeftClickEmptyC2S::new).encoder(PLeftClickEmptyC2S::toByte).consumerMainThread(PLeftClickEmptyC2S::handle).add();
//        INSTANCE.messageBuilder(PStackedOnMeC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PStackedOnMeC2S::new).encoder(PStackedOnMeC2S::toByte).consumerMainThread(PStackedOnMeC2S::handle).add();
//        INSTANCE.messageBuilder(PStackedOnOtherC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PStackedOnOtherC2S::new).encoder(PStackedOnOtherC2S::toByte).consumerMainThread(PStackedOnOtherC2S::handle).add();
        INSTANCE.messageBuilder(PAmbrosiumChargeC2S.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(PAmbrosiumChargeC2S::new).encoder(PAmbrosiumChargeC2S::toByte).consumerMainThread(PAmbrosiumChargeC2S::handle).add();
        INSTANCE.messageBuilder(ServerCursePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ServerCursePacket::new)
                .encoder(ServerCursePacket::ToByte)
                .consumerMainThread(ServerCursePacket::handle)
                .add();
        INSTANCE.messageBuilder(CurseTimeUpdatePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CurseTimeUpdatePacket::new)
                .encoder(CurseTimeUpdatePacket::ToByte)
                .consumerMainThread(CurseTimeUpdatePacket::handle)
                .add();
        INSTANCE.messageBuilder(StarHitPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(StarHitPacket::new)
                .encoder(StarHitPacket::ToByte)
                .consumerMainThread(StarHitPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendToClient(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
