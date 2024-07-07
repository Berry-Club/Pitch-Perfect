package dev.aaronhowser.mods.pitchperfect.serialization

import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.nio.file.Path

object SongSerializer {

    @Serializable
    data class Song(
        val beats: List<Beat>
    )

    @Serializable
    data class Beat(
        val delayBefore: Int = 1,
        val sounds: Map<NoteBlockInstrument, Float>
    )

    fun fromSongItemComponent(component: SongItemComponent): Song {
        val componentBeats = component.beats

        val newBeats = mutableListOf<Beat>()

        for (beat in componentBeats) {

            val sounds = mutableMapOf<NoteBlockInstrument, Float>()
            val delayBefore = beat.delayBefore

            newBeats.add(Beat(delayBefore, sounds))
        }

        return Song(newBeats)
    }

    fun save(song: Song, path: Path) {
        val json = Json {
            prettyPrint = true
        }

        val jsonString = json.encodeToString(Song.serializer(), song)

        path.toFile().writeText(jsonString)
    }

}