package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent

class SongWip(
    private var song: Song
) {

    companion object {
        val CODEC: Codec<SongWip> =
            Song.CODEC.xmap(::SongWip, SongWip::song)
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongWip> =
            Song.STREAM_CODEC.map(::SongWip, SongWip::song)
    }

    constructor() : this(Song(emptyMap()))

    private fun addBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ) {

        val updatedBeats = song.beats.toMutableMap()

        val currentBeats = updatedBeats[instrument].orEmpty()
        val currentBeat = currentBeats.find { it.at == delay } ?: Beat(delay, emptyList())
        val newNotes = currentBeat.notes + note
        val newBeat = Beat(delay, newNotes)

        updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat
        song = song.copy(beats = updatedBeats)
    }

    private fun removeBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ) {
        val updatedBeats = song.beats.toMutableMap()

        val currentBeats = updatedBeats[instrument] ?: return
        val currentBeat = currentBeats.find { it.at == delay } ?: return
        val newNotes = currentBeat.notes - note
        val newBeat = Beat(delay, newNotes)

        updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat

        song = song.copy(beats = updatedBeats)
    }

}