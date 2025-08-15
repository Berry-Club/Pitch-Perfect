package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.song.data.ComposerSongSavedData
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import java.util.*

class ComposerSong(
	val uuid: UUID,
	var song: Song
) {

	companion object {
		val CODEC: Codec<ComposerSong> =
			RecordCodecBuilder.create {
				it.group(
					OtherUtil.UUID_CODEC
						.fieldOf("uuid")
						.forGetter(ComposerSong::uuid),
					Song.CODEC
						.fieldOf("song")
						.forGetter(ComposerSong::song)
				).apply(it, ::ComposerSong)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSong> =
			StreamCodec.composite(
				OtherUtil.UUID_STREAM_CODEC, ComposerSong::uuid,
				Song.STREAM_CODEC, ComposerSong::song,
				::ComposerSong
			)
	}

	fun addBeat(
		delay: Int,
		note: Note,
		instrument: Holder<SoundEvent>,
		savedData: ComposerSongSavedData?
	) {
		val updatedBeats = song.beats.toMutableMap()

		val currentBeats = updatedBeats[instrument].orEmpty()
		val currentBeat = currentBeats.find { it.at == delay } ?: Beat(delay, emptyList())
		val newNotes = currentBeat.notes + note
		val newBeat = Beat(delay, newNotes)

		updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat

		val newSong = song.copy(beats = updatedBeats)
		song = newSong

		savedData?.setDirty()
	}

	fun removeBeat(
		delay: Int,
		note: Note,
		instrument: Holder<SoundEvent>,
		savedData: ComposerSongSavedData?
	) {
		val updatedBeats = song.beats.toMutableMap()

		val currentBeats = updatedBeats[instrument] ?: return
		val currentBeat = currentBeats.find { it.at == delay } ?: return
		val newNotes = currentBeat.notes - note

		if (newNotes.isNotEmpty()) {
			val newBeat = Beat(delay, newNotes)

			updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat
		} else {
			updatedBeats[instrument] = currentBeats.filterNot { it.at == delay }
			if (updatedBeats[instrument].isNullOrEmpty()) {
				updatedBeats.remove(instrument)
			}
		}

		val newSong = song.copy(beats = updatedBeats)
		song = newSong

		savedData?.setDirty()
	}

	fun getSoundsAt(
		delay: Int,
		pitch: Int
	): List<Holder<SoundEvent>> {
		val note = Note.getFromPitch(pitch)
		val list = mutableListOf<Holder<SoundEvent>>()

		for ((soundHolder, beats) in song.beats) {
			val beat = beats.find { it.at == delay } ?: continue

			for (beatNote in beat.notes) {
				if (beatNote == note) {
					list.add(soundHolder)
				}
			}
		}

		return list
	}

}