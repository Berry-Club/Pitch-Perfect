package dev.aaronhowser.mods.pitchperfect.song.parts

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent

class SongWip(
    private var song: Song
) {

    private fun Song.addBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ): Song {
        val currentBeatsWithThisInstrument = this.beats.getOrDefault(instrument, listOf())
        val beatAtDelay = currentBeatsWithThisInstrument.find { it.at == delay } ?: Beat(delay, listOf())

        val newNotes = beatAtDelay.notes + note
        val newBeat = Beat(delay, newNotes)
        val newBeats = currentBeatsWithThisInstrument - beatAtDelay + newBeat

        val allOtherInstruments = this.beats - instrument
        val finalMap = allOtherInstruments + (instrument to newBeats)
        return Song(finalMap)
    }

    private fun Song.removeBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ): Song {
        val currentBeatsWithThisInstrument = this.beats.getOrElse(instrument) { return this }
        val beatAtDelay = currentBeatsWithThisInstrument.find { it.at == delay } ?: return this

        val newNotes = beatAtDelay.notes - note
        val newBeat = Beat(delay, newNotes)
        val newBeats = currentBeatsWithThisInstrument - beatAtDelay + newBeat

        val allOtherInstruments = this.beats - instrument
        val finalMap = allOtherInstruments + (instrument to newBeats)
        return Song(finalMap)
    }

}