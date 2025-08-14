package dev.aaronhowser.mods.pitchperfect.song

import com.mojang.brigadier.StringReader
import dev.aaronhowser.mods.pitchperfect.song.parts.Beat
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.Song.Companion.ID_TO_INSTRUMENT
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.nio.file.Files
import java.nio.file.Path

object SongBuilder {

	fun fromFile(path: Path): Song? {
		try {
			val string = Files.readString(path)
			val song = fromString(string)

			return song
		} catch (e: Exception) {
			e.printStackTrace()
			return null
		}
	}

	fun fromString(string: String): Song? {
		return fromStringReader(StringReader(string))
	}

	fun fromStringReader(reader: StringReader): Song? {
		try {
			val beats = HashMap<Holder<SoundEvent>, List<Beat>>()

			reader.skipWhitespace()
			reader.expect('{')
			reader.skipWhitespace()

			while (reader.canRead() && reader.peek() != '}') {
				readInstrument(reader, beats)
				skipCommas(reader)
			}

			reader.expect('}')
			return Song(beats)
		} catch (e: Exception) {
			e.printStackTrace()
			return null
		}
	}

	private fun readInstrument(
		reader: StringReader,
		beats: MutableMap<Holder<SoundEvent>, List<Beat>>
	) {
		val instrumentName = reader.readStringUntil('=')
		val sound = getSoundHolder(instrumentName)

		reader.skipWhitespace()

		val beatList = readBeats(reader)

		if (beatList.isNotEmpty()) {
			beats[sound] = beatList
		}
	}

	private fun readBeats(reader: StringReader): MutableList<Beat> {
		val beatList = mutableListOf<Beat>()

		if (reader.canRead() && reader.peek() == '[') {
			reader.skip()

			while (reader.canRead() && reader.peek() != ']') {
				beatList.add(Beat.parse(reader))
				skipCommas(reader)
			}

			reader.expect(']')
		} else if (reader.canRead()) {
			beatList.add(Beat.parse(reader))
		}

		return beatList
	}

	private fun skipCommas(reader: StringReader) {
		while (reader.canRead() && reader.peek() == ',') {
			reader.skip()
			reader.skipWhitespace()
		}
	}

	fun getSoundHolder(instrumentName: String): Holder<SoundEvent> {
		val instrument: NoteBlockInstrument? = ID_TO_INSTRUMENT[instrumentName]

		return if (instrument == null) {
			BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(
				ResourceKey.create(
					Registries.SOUND_EVENT,
					ResourceLocation.parse(instrumentName)
				)
			)
		} else {
			instrument.soundEvent
		}
	}

}