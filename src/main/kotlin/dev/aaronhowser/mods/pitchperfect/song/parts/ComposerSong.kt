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
import net.minecraft.world.entity.player.Player
import java.util.*

class ComposerSong(
    val uuid: UUID,
    var songInfo: SongInfo,
) {

    companion object {
        val CODEC: Codec<ComposerSong> =
            RecordCodecBuilder.create {
                it.group(
                    UuidComponent.UUID_CODEC
                        .fieldOf("uuid")
                        .forGetter(ComposerSong::uuid),
                    SongInfo.CODEC
                        .fieldOf("song_info")
                        .forGetter(ComposerSong::songInfo)
                ).apply(it, ::ComposerSong)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSong> =
            StreamCodec.composite(
                UuidComponent.UUID_STREAM_CODEC, ComposerSong::uuid,
                SongInfo.STREAM_CODEC, ComposerSong::songInfo,
                ::ComposerSong
            )

        const val SONG_INFO_NBT = "song_info"
        const val UUID_NBT = "uuid"

        fun fromCompoundTag(tag: CompoundTag): ComposerSong? {
            val songInfoTag = tag.get(SONG_INFO_NBT) as? CompoundTag ?: return null
            val singInfo = SongInfo.fromCompoundTag(songInfoTag) ?: return null

            val uuid = tag.getUuidOrNull(UUID_NBT) ?: return null

            return ComposerSong(uuid, singInfo)
        }
    }

    fun addBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ) {
        val updatedBeats = songInfo.song.beats.toMutableMap()

        val currentBeats = updatedBeats[instrument].orEmpty()
        val currentBeat = currentBeats.find { it.at == delay } ?: Beat(delay, emptyList())
        val newNotes = currentBeat.notes + note
        val newBeat = Beat(delay, newNotes)

        updatedBeats[instrument] = currentBeats.filterNot { it.at == delay } + newBeat

        val newSongInfo = songInfo.copy(song = songInfo.song.copy(beats = updatedBeats))
        songInfo = newSongInfo
    }

    fun removeBeat(
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ) {
        val updatedBeats = songInfo.song.beats.toMutableMap()

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

        val newSongInfo = songInfo.copy(song = songInfo.song.copy(beats = updatedBeats))
        songInfo = newSongInfo
    }

    fun getSoundsAt(
        delay: Int,
        pitch: Int
    ): List<Holder<SoundEvent>> {
        val note = Note.getFromPitch(pitch)
        val list = mutableListOf<Holder<SoundEvent>>()

        for ((soundHolder, beats) in songInfo.song.beats) {
            val beat = beats.find { it.at == delay } ?: continue

            for (beatNote in beat.notes) {
                if (beatNote == note) {
                    list.add(soundHolder)
                }
            }
        }

        return list
    }

    fun addAuthor(
        player: Player
    ) {
        addAuthor(player.uuid, player.name.string)
    }

    fun addAuthor(
        uuid: UUID,
        name: String
    ) {
        val authors = songInfo.authors.toMutableList()

        if (authors.none { it.uuid == uuid }) {
            authors.add(Author(uuid, name))
            songInfo = songInfo.copy(authors = authors)
        }
    }

    fun toTag(): Tag {
        val tag = CompoundTag()
        tag.putUUID(UUID_NBT, uuid)
        tag.put(SONG_INFO_NBT, songInfo.toTag())
        return tag
    }

}