package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

class SongBuilder(
    val startingTick: Long
) {

    private val map: MutableMap<Long, MutableMap<NoteBlockInstrument, List<Float>>> = mutableMapOf()

    fun addNote(
        currentTick: Long,
        instrument: NoteBlockInstrument,
        pitch: Float
    ) {
        val currentTickMap = map.getOrDefault(currentTick, mutableMapOf())
        val currentPitches = currentTickMap.getOrDefault(instrument, mutableListOf())

        val newPitches = currentPitches + pitch
        currentTickMap[instrument] = newPitches

        map[currentTick] = currentTickMap   // Is this one required?
    }

    fun build(): SongItemComponent {
        var ticksSoFar = 0L

        val beats = mutableListOf<SongItemComponent.SoundsWithDelayAfter>()

        for ((tick, tickNotes) in map) {
            val ticksAfterStart = tick - startingTick
            ticksSoFar += ticksAfterStart

            val sounds = tickNotes.toMap()
            val delayAfter = ticksAfterStart.toInt()

            beats.add(SongItemComponent.SoundsWithDelayAfter(sounds, delayAfter))
        }

        return SongItemComponent(beats)
    }

}