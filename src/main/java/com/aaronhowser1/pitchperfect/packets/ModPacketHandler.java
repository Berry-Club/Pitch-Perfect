package com.aaronhowser1.pitchperfect.packets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

//I understand basically none of this, thank you Tslat for having easy-to-read, open code 🙏

public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("pitchperfect", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void setup() {
        int id = 0;

        INSTANCE.registerMessage(id++, NoteParticleSpawnPacket.class, NoteParticleSpawnPacket::encode, NoteParticleSpawnPacket::decode, NoteParticleSpawnPacket::receiveMessage);
    }

    public static void messageNearbyPlayers(ModPacket packet, ServerLevel serverLevel, Vec3 origin, double radius) {
        for (ServerPlayer player : serverLevel.players()) {
            double distance = player.distanceToSqr(origin.x(), origin.y(), origin.z());
            if (distance < (radius * radius)) {
                messagePlayer(player, packet);
            }
        }
    }

    public static void messagePlayer(ServerPlayer player, ModPacket packet) {
        if (player.connection != null) {
            INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    packet
            );
        }
    }
}
