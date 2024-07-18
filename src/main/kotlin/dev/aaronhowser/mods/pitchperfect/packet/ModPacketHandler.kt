package dev.aaronhowser.mods.pitchperfect.packet

import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnElectricLinePacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler

object ModPacketHandler {

    fun registerPayloads(event: RegisterPayloadHandlersEvent) {

        val registrar = event.registrar("1")

        registrar.playToClient(
            SpawnNotePacket.TYPE,
            SpawnNotePacket.STREAM_CODEC,
            DirectionalPayloadHandler(
                { packet, context -> packet.receiveMessage(context) },
                { packet, context -> packet.receiveMessage(context) }
            )
        )

        registrar.playToClient(
            SpawnElectricLinePacket.TYPE,
            SpawnElectricLinePacket.STREAM_CODEC,
            DirectionalPayloadHandler(
                { packet, context -> packet.receiveMessage(context) },
                { packet, context -> packet.receiveMessage(context) }
            )
        )

        registrar.playToServer(
            ClickComposerCellPacket.TYPE,
            ClickComposerCellPacket.STREAM_CODEC,
            DirectionalPayloadHandler(
                { packet, context -> packet.receiveMessage(context) },
                { packet, context -> packet.receiveMessage(context) }
            )
        )

    }


    fun messageNearbyPlayers(packet: IModPacket, serverLevel: ServerLevel, origin: Vec3, radius: Double) {
        for (player in serverLevel.players()) {
            val distance = player.distanceToSqr(origin.x(), origin.y(), origin.z())
            if (distance < radius * radius) {
                messagePlayer(player, packet)
            }
        }
    }

    fun messagePlayer(player: ServerPlayer, packet: IModPacket) {
        PacketDistributor.sendToPlayer(player, packet)
    }

    fun messageAllPlayers(packet: IModPacket) {
        PacketDistributor.sendToAllPlayers(packet)
    }

    fun messageServer(packet: IModPacket) {
        PacketDistributor.sendToServer(packet)
    }

}