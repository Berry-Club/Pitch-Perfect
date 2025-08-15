package dev.aaronhowser.mods.pitchperfect.packet

import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ClickComposerCellPacket
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.ComposerPasteSongPacket
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.SongPasteCommandResponsePacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SetCurrentComposerSongPacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SongPasteCommandRequestPacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnElectricLinePacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object ModPacketHandler {

	fun registerPayloads(event: RegisterPayloadHandlersEvent) {

		val registrar = event.registrar("1")

		toClient(
			registrar,
			SpawnNotePacket.TYPE,
			SpawnNotePacket.STREAM_CODEC
		)

		toClient(
			registrar,
			SpawnElectricLinePacket.TYPE,
			SpawnElectricLinePacket.STREAM_CODEC
		)

		toServer(
			registrar,
			ClickComposerCellPacket.TYPE,
			ClickComposerCellPacket.STREAM_CODEC
		)

		toServer(
			registrar,
			ComposerPasteSongPacket.TYPE,
			ComposerPasteSongPacket.STREAM_CODEC
		)

		toClient(
			registrar,
			SongPasteCommandRequestPacket.TYPE,
			SongPasteCommandRequestPacket.STREAM_CODEC
		)

		toServer(
			registrar,
			SongPasteCommandResponsePacket.TYPE,
			SongPasteCommandResponsePacket.STREAM_CODEC
		)

		toClient(
			registrar,
			SetCurrentComposerSongPacket.TYPE,
			SetCurrentComposerSongPacket.STREAM_CODEC
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
		PacketDistributor.sendToPlayer(player, packet)
	}

	fun messageAllPlayers(packet: ModPacket) {
		PacketDistributor.sendToAllPlayers(packet)
	}

	fun messageServer(packet: ModPacket) {
		PacketDistributor.sendToServer(packet)
	}

	private fun <T : ModPacket> toClient(
		registrar: PayloadRegistrar,
		packetType: CustomPacketPayload.Type<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
	) {
		registrar.playToClient(
			packetType,
			streamCodec
		) { packet, context -> packet.receiveOnClient(context) }
	}

	private fun <T : ModPacket> toServer(
		registrar: PayloadRegistrar,
		packetType: CustomPacketPayload.Type<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
	) {
		registrar.playToServer(
			packetType,
			streamCodec
		) { packet, context -> packet.receiveOnServer(context) }
	}

}