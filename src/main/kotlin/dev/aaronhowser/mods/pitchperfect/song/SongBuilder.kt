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



    }

}