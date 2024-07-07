package dev.aaronhowser.mods.pitchperfect.serialization

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent.SoundsWithDelayAfter.Companion.PITCH_LIST_CODEC
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Files

object SongSerializer {

    data class Song(
        @SerializedName("beats")
        val beats: List<Beat>
    ) {

        companion object {
            val CODEC: Codec<Song> =
                Beat.CODEC.listOf().xmap(::Song, Song::beats)
        }

    }

    data class Beat(
        @SerializedName("delay_before")
        val delayBefore: Int = 1,
        @SerializedName("sounds")
        val sounds: Map<NoteBlockInstrument, List<Float>>
    ) {
        companion object {
            val CODEC: Codec<Beat> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.optionalFieldOf("delay_before", 1).forGetter(Beat::delayBefore),
                    Codec.unboundedMap(
                        StringRepresentable.fromEnum(NoteBlockInstrument::values),
                        PITCH_LIST_CODEC
                    ).fieldOf("sounds").forGetter(Beat::sounds)
                ).apply(instance, ::Beat)
            }
        }
    }

    private val gson = GsonBuilder().setPrettyPrinting().setLenient().serializeNulls().disableHtmlEscaping().create()

    fun fromSongItemComponent(component: SongItemComponent): Song {
        val componentBeats = component.beats

        val newBeats = mutableListOf<Beat>()

        for (beat in componentBeats) {

            val sounds = beat.sounds
            val delayBefore = beat.delayBefore

            newBeats.add(Beat(delayBefore, sounds))
        }

        return Song(newBeats)
    }

    fun save(song: Song) {
        val jsonString = gson.toJson(Song.CODEC.encodeStart(JsonOps.INSTANCE, song).getOrThrow())
        val path = FMLPaths.CONFIGDIR.get().resolve("song.json")

        Files.write(path, jsonString.toByteArray())
    }

}