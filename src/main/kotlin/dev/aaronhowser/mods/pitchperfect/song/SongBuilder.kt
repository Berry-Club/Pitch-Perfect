package dev.aaronhowser.mods.pitchperfect.song

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.nio.file.Path

class SongBuilder(
    private val startingTick: Long
) {

    private val notesMap: MutableMap<NoteBlockInstrument, Map<
            Int,            // Tick
            List<Float>>    // Pitches
            > = mutableMapOf()

    fun addNote(
        currentTick: Long,
        instrument: NoteBlockInstrument,
        pitch: Float
    ) {
        val ticksSinceStart = (currentTick - startingTick).toInt()

        val previousInstrumentBeats = notesMap[instrument] ?: emptyMap()
        val previousBeatsThisTick = previousInstrumentBeats[ticksSinceStart] ?: emptyList()
        val withThisPitch = previousBeatsThisTick + pitch
        val newInstrumentBeats = previousInstrumentBeats + (ticksSinceStart to withThisPitch)

        notesMap[instrument] = newInstrumentBeats
    }

    fun build(path: Path? = null): SongSerializer.Song {
        val instrumentBeatMap = mutableMapOf<NoteBlockInstrument, List<SongSerializer.Beat>>()

        for ((sound, map) in notesMap) {
            val instrumentBeats = mutableListOf<SongSerializer.Beat>()

            for ((tick, pitches) in map) {
                val notes: MutableList<SongSerializer.Note> = mutableListOf()
                for (pitch in pitches) {
                    val note = SongSerializer.Note.getFromPitch(pitch)

                    notes.add(note)
                }
                val beat = SongSerializer.Beat(tick, notes)
                instrumentBeats.add(beat)
            }

            instrumentBeatMap[sound] = instrumentBeats
        }

        val song = SongSerializer.Song(instrumentBeatMap)

        if (path != null) {
            song.saveToPath(path)
        }

        return song
    }

}