package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import dev.aaronhowser.mods.pitchperfect.song.data.ComposerSongSavedData
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import java.util.*

class ComposerSong(
    val uuid: UUID
) {

    constructor() : this(UUID.randomUUID())

    companion object {
        val CODEC: Codec<ComposerSong> =
            UuidComponent.UUID_CODEC.xmap(::ComposerSong, ComposerSong::uuid)

        val STREAM_CODEC: StreamCodec<ByteBuf, ComposerSong> =
            UuidComponent.UUID_STREAM_CODEC.map(::ComposerSong, ComposerSong::uuid)
    }

    fun addBeat(
        composerSongSavedData: ComposerSongSavedData,
        delay: Int,
        note: Note,
        instrument: Holder<SoundEvent>
    ) {
        val songInfo = composerSongSavedData.getSong(uuid)

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

}