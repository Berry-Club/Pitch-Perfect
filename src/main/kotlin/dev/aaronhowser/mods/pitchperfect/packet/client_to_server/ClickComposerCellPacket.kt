package dev.aaronhowser.mods.pitchperfect.packet.client_to_server

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.packet.ModPacket
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SetCurrentComposerSongPacket
import dev.aaronhowser.mods.pitchperfect.song.data.ComposerSongSavedData
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClickComposerCellPacket(
	val delay: Int,
	val pitch: Int,
	val leftClick: Boolean,
	val selectedInstrument: String,
	val blockPos: BlockPos
) : ModPacket() {

	override fun handleOnServer(context: IPayloadContext) {
		val player = context.player() as? ServerPlayer ?: return

		val composerBlockEntity = player.level().getBlockEntity(blockPos) as? ComposerBlockEntity
			?: return

		val playerReach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)
		if (!player.canInteractWithBlock(blockPos, playerReach)) return

		val composerSongUuid = composerBlockEntity.composerSongUuid

		val composerSongSavedData = ComposerSongSavedData.get(player.serverLevel())
		val composerSong = composerSongSavedData.getOrCreateSong(composerSongUuid)

		val note = Note.getFromPitch(pitch)
		val soundHolder = Song.getSoundHolder(selectedInstrument)

		if (leftClick) {
			composerSong.addBeat(delay, note, soundHolder, composerSongSavedData)
		} else {
			composerSong.removeBeat(delay, note, soundHolder, composerSongSavedData)
		}

		ModPacketHandler.messagePlayer(player, SetCurrentComposerSongPacket(composerSong))
	}

	override fun type(): CustomPacketPayload.Type<ClickComposerCellPacket> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<ClickComposerCellPacket> =
			CustomPacketPayload.Type(OtherUtil.modResource("click_composer_cell"))

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ClickComposerCellPacket> =
			StreamCodec.composite(
				ByteBufCodecs.VAR_INT, ClickComposerCellPacket::delay,
				ByteBufCodecs.VAR_INT, ClickComposerCellPacket::pitch,
				ByteBufCodecs.BOOL, ClickComposerCellPacket::leftClick,
				ByteBufCodecs.STRING_UTF8, ClickComposerCellPacket::selectedInstrument,
				BlockPos.STREAM_CODEC, ClickComposerCellPacket::blockPos,
				::ClickComposerCellPacket
			)

	}

}