package com.aaronhowser1.pitchperfect.packets;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
}
