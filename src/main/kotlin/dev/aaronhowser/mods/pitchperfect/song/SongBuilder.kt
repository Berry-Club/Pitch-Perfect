package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

class SongBuilder(
    val startingTick: Long
) {

    private val tickToNotesMap: MutableMap<Long, MutableMap<NoteBlockInstrument, List<Float>>> = mutableMapOf()

    fun addNote(
        currentTick: Long,
        instrument: NoteBlockInstrument,
        pitch: Float
    ) {
        val currentTickMap = tickToNotesMap.getOrDefault(currentTick, mutableMapOf())
        val currentPitches = currentTickMap.getOrDefault(instrument, mutableListOf())

        val newPitches = currentPitches + pitch
        currentTickMap[instrument] = newPitches

        tickToNotesMap[currentTick] = currentTickMap   // Is this one required?
    }

    fun build(): SongItemComponent {
        var previousTick = startingTick

        val beats = mutableListOf<SongItemComponent.SoundsWithDelayAfter>()

        for ((tick, tickNotes) in tickToNotesMap) {
            val delay = tick - previousTick
            previousTick = tick

            val sounds = tickNotes.toMap()

            beats.add(SongItemComponent.SoundsWithDelayAfter(sounds, delay.toInt()))
        }

        return SongItemComponent(beats)
    }

    override fun toString(): String {
        return "SongBuilder(startingTick=$startingTick, tickToNotesMap=$tickToNotesMap)"
    }

}