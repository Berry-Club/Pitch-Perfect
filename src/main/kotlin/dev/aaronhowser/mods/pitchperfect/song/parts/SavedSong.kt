package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModMessageLang
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.player.Player

data class SavedSong(
	val title: String,
	val author: Component,
	val song: Song
) {

	constructor(
		title: String,
		player: Player,
		song: Song
	) : this(title, player.name, song)

	fun getComponent(): Component {
		val uuidComponent = ModLanguageProvider.Misc.SONG_UUID.toComponent()
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
							song.uuid.toString()
						)
					)
			}

		val authorsHoverComponent = author

		val authorsComponent = ModLanguageProvider.Misc.SONG_AUTHORS.toComponent()
			.withStyle {
				it.withHoverEvent(
					HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						authorsHoverComponent
					)
				)
			}

		var songString = song.toString()
		if (songString.length > 500) {
			songString = songString.take(500) + "..."
		}

		val songDataComponent = ModLanguageProvider.Misc.SONG_RAW.toComponent()
			.withStyle {
				it
					.withHoverEvent(
						HoverEvent(
							HoverEvent.Action.SHOW_TEXT,
							ModMessageLang.CLICK_COPY_RAW_SONG.toComponent(
								songString
							)
						)
					)
					.withClickEvent(
						ClickEvent(
							ClickEvent.Action.COPY_TO_CLIPBOARD,
							song.toString()
						)
					)
			}

		val playComponent = ModLanguageProvider.Misc.SONG_PLAY.toComponent()
			.withStyle {
				it
					.withHoverEvent(
						HoverEvent(
							HoverEvent.Action.SHOW_TEXT,
							ModMessageLang.CLICK_PLAY_SONG.toComponent()
						)
					)
					.withClickEvent(
						ClickEvent(
							ClickEvent.Action.RUN_COMMAND,
							"/pitchperfect playSong ${song.uuid}"
						)
					)
			}

		return ModLanguageProvider.Misc.SONG_INFO.toComponent(
			title,
			authorsComponent,
			uuidComponent,
			songDataComponent,
			playComponent,
		)
	}

	companion object {
		val CODEC: Codec<SavedSong> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.STRING
						.fieldOf("title:")
						.forGetter(SavedSong::title),
					ComponentSerialization.CODEC
						.fieldOf("author")
						.forGetter(SavedSong::author),
					Song.CODEC
						.fieldOf("song")
						.forGetter(SavedSong::song)
				).apply(instance, ::SavedSong)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SavedSong> =
			StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, SavedSong::title,
				ComponentSerialization.STREAM_CODEC, SavedSong::author,
				Song.STREAM_CODEC, SavedSong::song,
				::SavedSong
			)
	}

}