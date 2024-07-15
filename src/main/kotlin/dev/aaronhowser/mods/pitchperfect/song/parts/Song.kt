package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
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


data class Song(
    val beats: Map<Holder<SoundEvent>, List<Beat>>
) {

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

        // What the hell does this even do Lat
        private fun <K, V> beatMap(size: Int): Map<K, V> {
            return HashMap(size)
        }

        val CODEC: Codec<Song> = Codec.STRING.flatXmap(
            { string: String ->
                try {
                    return@flatXmap DataResult.success(parse(string))
                } catch (exception: Exception) {
                    return@flatXmap DataResult.error { "Failed to parse song: " + exception.message }
                }
            },
            { song: Song ->
                DataResult.success(
                    song.toString()
                )
            })

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Song> = ByteBufCodecs.map(
            Song::beatMap,
            SoundEvent.STREAM_CODEC,
            Beat.STREAM_CODEC.apply(ByteBufCodecs.list())
        ).map(::Song, Song::beats)

        fun parse(string: String): Song {
            return parse(StringReader(string))
        }

        fun parse(reader: StringReader): Song {

            val beats: HashMap<Holder<SoundEvent>, List<Beat>> = HashMap()

            reader.skipWhitespace()
            reader.expect('{')
            reader.skipWhitespace()

            while (reader.canRead() && reader.peek() != '}') {
                val instrumentName: String = reader.readStringUntil('=')
                val instrument: NoteBlockInstrument? = ID_TO_INSTRUMENT[instrumentName]

                val sound: Holder<SoundEvent> = if (instrument == null) {
                    BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(
                        ResourceKey.create(
                            Registries.SOUND_EVENT,
                            ResourceLocation.parse(instrumentName)
                        )
                    )
                } else {
                    instrument.soundEvent
                }

                reader.skipWhitespace()

                val beatList = ArrayList<Beat>()

                if (reader.canRead() && reader.peek() == '[') {
                    reader.skip()
                    while (reader.canRead() && reader.peek() != ']') {
                        beatList.add(Beat.parse(reader))

                        while (reader.canRead() && reader.peek() == ',') {
                            reader.skip()
                            reader.skipWhitespace()
                        }
                    }
                } else if (reader.canRead()) {
                    beatList.add(Beat.parse(reader))
                }

                if (beatList.isNotEmpty()) {
                    beats[sound] = beatList
                }

                while (reader.canRead() && reader.peek() == ',') {
                    reader.skip()
                    reader.skipWhitespace()
                }
            }

            reader.expect('}')
            return Song(beats)
        }

        fun fromFile(path: Path): Song? {
            try {

                val string = Files.readString(path)
                val song = parse(string)

                return song
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append('{')

        var first = true

        for ((sound, beats) in beats.entries) {

            if (first) {
                first = false
            } else {
                stringBuilder.append(',')
            }

            val instrument: NoteBlockInstrument? = SOUND_TO_INSTRUMENT[sound.key]

            if (instrument != null) {
                stringBuilder.append(instrument.serializedName)
            } else {
                stringBuilder.append(sound.key!!.location())
            }

            stringBuilder.append('=')

            if (beats.size == 1) {
                stringBuilder.append(beats.first())
            } else {
                stringBuilder.append('[')

                for (i in beats.indices) {
                    if (i != 0) {
                        stringBuilder.append(',')
                    }

                    stringBuilder.append(beats[i])
                }

                stringBuilder.append(']')
            }
        }

        stringBuilder.append('}')
        return stringBuilder.toString()
    }

    fun saveToPath(path: Path) {
        try {
            val string = toString()
            Files.write(path, string.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}