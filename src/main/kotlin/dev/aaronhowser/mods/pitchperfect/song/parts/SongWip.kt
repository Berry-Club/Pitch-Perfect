package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent

class SongWip(
    var song: Song
) {

    companion object {
        val CODEC: Codec<SongWip> =
            Song.CODEC.xmap(::SongWip, SongWip::song)
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongWip> =
            Song.STREAM_CODEC.map(::SongWip, SongWip::song)

        private const val SONG_NBT = "song"

        fun fromCompoundTag(tag: CompoundTag): SongWip? {
            val songString = tag.getString(SONG_NBT)
            if (songString.isEmpty()) return null
            val song = Song.fromString(songString) ?: return null
            return SongWip(song)
        }
    }

    constructor() : this(Song(emptyMap()))

    fun addBeat(
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

    fun removeBeat(
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

    fun getSoundsAt(
        delay: Int,
        pitch: Int
    ): List<Holder<SoundEvent>> {
        val note = Note.getFromPitch(pitch)
        val list = mutableListOf<Holder<SoundEvent>>()

        for ((soundHolder, beats) in song.beats) {
            val beat = beats.find { it.at == delay } ?: continue

            for (beatNote in beat.notes) {
                if (beatNote == note) {
                    list.add(soundHolder)
                }
            }
        }

        return list
    }

    fun getSoundStringsAt(
        delay: Int,
        pitch: Int
    ): List<String> {
        return getSoundsAt(delay, pitch).map { it.value().location.toString() }
    }

    fun toTag(): Tag {
        val tag = CompoundTag()
        tag.putString(SONG_NBT, song.toString())
        return tag
    }

}