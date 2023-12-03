package com.aaronhowser1.pitchperfect.packet

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel

object ModPacketHandler {

    private const val PROTOCOL_VERSION = "1"
    private val INSTANCE: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ResourceLocation("pitchperfect", "main"),
        { PROTOCOL_VERSION },
        { anObject: String? -> PROTOCOL_VERSION == anObject },
        { anObject: String? -> PROTOCOL_VERSION == anObject }
    )

    fun setup() {
        var id = 0

        INSTANCE.registerMessage(
            ++id,
            SpawnNoteParticlePacket::class.java,
            SpawnNoteParticlePacket::encode,
            SpawnNoteParticlePacket::decode,
            SpawnNoteParticlePacket::receiveMessage
        )

        INSTANCE.registerMessage(
            ++id,
            SpawnElectricParticlePacket::class.java,
            SpawnElectricParticlePacket::encode,
            SpawnElectricParticlePacket::decode,
            SpawnElectricParticlePacket::receiveMessage
        )

        INSTANCE.registerMessage(
            ++id,
            SpawnElectricPathPacket::class.java,
            SpawnElectricPathPacket::encode,
            SpawnElectricPathPacket::decode,
            SpawnElectricPathPacket::receiveMessage
        )
    }

    fun messageNearbyPlayers(packet: ModPacket, serverLevel: ServerLevel, origin: Vec3, radius: Double) {
        for (player in serverLevel.players()) {
            val distance = player.distanceToSqr(origin.x(), origin.y(), origin.z())
            if (distance < radius * radius) {
                messagePlayer(player, packet)
            }
        }
    }

    fun messagePlayer(player: ServerPlayer, packet: ModPacket) {
        INSTANCE.send<Any?>(
            PacketDistributor.PLAYER.with { player },
            packet
        )
    }
}