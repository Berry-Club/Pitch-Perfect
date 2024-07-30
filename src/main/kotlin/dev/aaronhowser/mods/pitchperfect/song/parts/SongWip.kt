package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import java.util.*

class SongWip(
    val uuid: UUID,
    var song: Song
) {

    companion object {
        val CODEC: Codec<SongWip> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    UuidComponent.UUID_CODEC.fieldOf(UUID_NBT).forGetter(SongWip::uuid),
                    Song.CODEC.fieldOf(SONG_NBT).forGetter(SongWip::song)
                ).apply(instance, ::SongWip)
            }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongWip> =
            StreamCodec.composite(
                UuidComponent.UUID_STREAM_CODEC, SongWip::uuid,
                Song.STREAM_CODEC, SongWip::song,
                ::SongWip
            )

        private const val SONG_NBT = "song"
        private const val UUID_NBT = "uuid"

        fun fromCompoundTag(tag: CompoundTag): SongWip? {
            val songString = tag.getString(SONG_NBT)
            if (songString.isEmpty()) return null
            val song = Song.fromString(songString) ?: return null

            val uuid = tag.getUuidOrNull(UUID_NBT) ?: return null

            return SongWip(uuid, song)
        }
    }

    constructor() : this(UUID.randomUUID(), Song(emptyMap()))

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

        if (newNotes.isNotEmpty()) {
            val newBeat = Beat(delay, newNotes)

            updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat
        } else {
            updatedBeats[instrument] = currentBeats.filterNot { it.at == delay }
            if (updatedBeats[instrument].isNullOrEmpty()) {
                updatedBeats.remove(instrument)
            }
        }

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

    fun toTag(): Tag {
        val tag = CompoundTag()
        tag.putString(SONG_NBT, song.toString())
        return tag
    }

}