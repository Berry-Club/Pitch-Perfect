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

        val updatedBeats = this.beats.toMutableMap()

        val currentBeats = updatedBeats[instrument].orEmpty()
        val currentBeat = currentBeats.find { it.at == delay } ?: Beat(delay, emptyList())
        val newNotes = currentBeat.notes + note
        val newBeat = Beat(delay, newNotes)

        updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat
        return this.copy(beats = updatedBeats)
    }

    private fun Song.removeBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ): Song {
        val updatedBeats = this.beats.toMutableMap()

        val currentBeats = updatedBeats[instrument] ?: return this
        val currentBeat = currentBeats.find { it.at == delay } ?: return this
        val newNotes = currentBeat.notes - note
        val newBeat = Beat(delay, newNotes)

        updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat

        return this.copy(beats = updatedBeats)
    }

}