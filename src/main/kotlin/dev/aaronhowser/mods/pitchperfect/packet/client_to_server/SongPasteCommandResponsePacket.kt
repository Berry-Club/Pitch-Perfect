package dev.aaronhowser.mods.pitchperfect.packet.client_to_server

import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModMessageLang
import dev.aaronhowser.mods.pitchperfect.packet.ModPacket
import dev.aaronhowser.mods.pitchperfect.song.data.SongSavedData
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class SongPasteCommandResponsePacket(
	val title: String,
	val clipboard: String
) : ModPacket() {

	override fun handleOnServer(context: IPayloadContext) {
		val player = context.player() as ServerPlayer
		val song = Song.fromString(clipboard)

		if (song == null) {
			player.sendSystemMessage(ModMessageLang.SONG_PASTE_FAIL_TO_PARSE.toComponent(clipboard))
			return
		}

		val songInfo = SongInfo(
			title,
			player,
			song
		)

		val songSavedData = SongSavedData.get(player.serverLevel())
		val result = songSavedData.addSongInfo(songInfo)

		val component = if (result.success) {
			ModMessageLang.SONG_PASTE_ADDED
				.toComponent(title)
				.withStyle {
					it
						.withHoverEvent(
							HoverEvent(
								HoverEvent.Action.SHOW_TEXT,
								ModMessageLang.CLICK_COPY_SONG_UUID.toComponent(song.uuid.toString())
							)
						)
						.withClickEvent(
							ClickEvent(
								ClickEvent.Action.COPY_TO_CLIPBOARD,
								songInfo.song.uuid.toString()
							)
						)
				}
		} else {
			ModMessageLang.SONG_PASTE_FAIL_DUPLICATE.toComponent(title)
				.append(result.songInfo.getComponent())
		}

		player.sendSystemMessage(component)
	}

	override fun type(): CustomPacketPayload.Type<SongPasteCommandResponsePacket> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<SongPasteCommandResponsePacket> =
			CustomPacketPayload.Type(OtherUtil.modResource("song_paste_response"))

		val STREAM_CODEC: StreamCodec<ByteBuf, SongPasteCommandResponsePacket> = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, SongPasteCommandResponsePacket::title,
			ByteBufCodecs.STRING_UTF8, SongPasteCommandResponsePacket::clipboard,
			::SongPasteCommandResponsePacket
		)
	}

}