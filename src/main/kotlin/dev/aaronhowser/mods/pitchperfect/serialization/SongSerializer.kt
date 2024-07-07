package dev.aaronhowser.mods.pitchperfect.serialization

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.nio.file.Files
import java.nio.file.Path

object SongSerializer {

    data class Song(
        @SerializedName("beats")
        val beats: List<Beat>
    )

    data class Beat(
        @SerializedName("delay_before")
        val delayBefore: Int = 1,
        @SerializedName("sounds")
        val sounds: Map<NoteBlockInstrument, List<Float>>
    )

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

    fun save(song: Song, path: Path) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(song)
        Files.write(path, jsonString.toByteArray())
    }

}