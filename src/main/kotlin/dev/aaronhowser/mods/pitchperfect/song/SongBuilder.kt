package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.serialization.LatvianWhy
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

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

    fun build(): LatvianWhy.Song {

        val instrumentBeatMap = mutableMapOf<NoteBlockInstrument, List<LatvianWhy.Beat>>()

        for ((sound, map) in notesMap) {
            val instrumentBeats = mutableListOf<LatvianWhy.Beat>()

            for ((tick, pitches) in map) {
                for (pitch in pitches) {
                    val note = LatvianWhy.Note.entries.first { it.playSoundPitch == pitch }

                    println(
                        """
                        A new note:
                        - Pitch Float: $pitch
                        - Note: $note
                        - NoteDisplayName: ${note.displayName}
                        - PlaySoundPitch: ${note.playSoundPitch}
                        - NoteNote: ${note.note}
                        - NoteOctave: ${note.octave}
                        - NotePitch: ${note.pitch}
                    """.trimIndent()
                    )

                    val beat = LatvianWhy.Beat(tick, listOf(note))
                    instrumentBeats.add(beat)
                }
            }

            instrumentBeatMap[sound] = instrumentBeats
        }

        return LatvianWhy.Song(instrumentBeatMap)
    }

}