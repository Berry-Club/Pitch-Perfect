package dev.aaronhowser.mods.pitchperfect.song.parts

import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.jvm.optionals.getOrNull

typealias SerializableSong = Map<String, List<Beat>>

/**
 * Each song has a list of instruments, each with a list of Beats.
 *
 * A Beat is a timestamp and a list of Notes to play at that time.
 */

data class Song(
	val beats: Map<Holder<SoundEvent>, List<Beat>>
) {

	constructor() : this(emptyMap())

	val soundHolders: Set<Holder<SoundEvent>> = beats.keys

	val uuid: UUID = UUID.nameUUIDFromBytes(toString().toByteArray())

	override fun toString(): String {
		val result = CODEC.encodeStart(JsonOps.INSTANCE, this)
		return result.result().getOrNull().toString()
	}

	fun saveToPath(path: Path) {
		try {
			val string = toString()
			Files.write(path, string.toByteArray())
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	companion object {
		val defaultFile = FMLPaths.CONFIGDIR.get().resolve("song.txt")

		val ID_TO_INSTRUMENT: HashMap<String, NoteBlockInstrument> = HashMap()
		val SOUND_TO_INSTRUMENT: IdentityHashMap<ResourceKey<SoundEvent>, NoteBlockInstrument> = IdentityHashMap()

		init {
			for (instrument in NoteBlockInstrument.entries) {
				ID_TO_INSTRUMENT[instrument.serializedName] = instrument

				if (instrument.isTunable) {
					SOUND_TO_INSTRUMENT[instrument.soundEvent.key] = instrument
				}
			}
		}

		val CODEC: Codec<Song> =
			Codec.unboundedMap(Codec.STRING, Beat.CODEC.listOf())
				.xmap(
					::fromRlMap,
					::toRlMap
				)

		private fun toRlMap(song: Song): SerializableSong {
			val result = mutableMapOf<String, List<Beat>>()

			for ((holder, beats) in song.beats) {
				val name = getSoundString(holder)
				result[name] = beats
			}

			return result
		}

		private fun fromRlMap(map: SerializableSong): Song {
			val result = mutableMapOf<Holder<SoundEvent>, List<Beat>>()

			for ((locationString, beats) in map) {
				val soundHolder = getSoundHolder(locationString)
				result[soundHolder] = beats
			}

			return Song(result)
		}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Song> =
			ByteBufCodecs.map(
				OtherUtil::weirdMapFunctionThingy,
				SoundEvent.STREAM_CODEC,
				Beat.STREAM_CODEC.apply(ByteBufCodecs.list())
			).map(::Song, Song::beats)

		fun fromTag(tag: Tag): Song? {
			val result = CODEC.parse(NbtOps.INSTANCE, tag)
			return result.result().getOrNull()
		}

		fun fromString(string: String): Song? {
			val json = JsonParser.parseString(string)
			val result = CODEC.parse(JsonOps.INSTANCE, json)
			return result.result().getOrNull()
		}

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

		fun getSoundString(sound: Holder<SoundEvent>): String {
			val instrument: NoteBlockInstrument? = SOUND_TO_INSTRUMENT[sound.key]

			return if (instrument == null) {
				sound.value().location.toString()
			} else {
				instrument.serializedName
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

}