package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import java.util.*

class ComposerSong(
    var song: Song,
    var authors: List<Author>
) {

    companion object {
        val CODEC: Codec<ComposerSong> =
            RecordCodecBuilder.create {
                it.group(
                    Song.CODEC.fieldOf("song").forGetter(ComposerSong::song),
                    Author.CODEC.listOf().fieldOf("authors").forGetter(ComposerSong::authors)
                ).apply(it, ::ComposerSong)
            }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSong> =
            StreamCodec.composite(
                Song.STREAM_CODEC, ComposerSong::song,
                Author.STREAM_CODEC.apply(ByteBufCodecs.list()), ComposerSong::authors,
                ::ComposerSong
            )

        private const val SONG_NBT = "song"
        private const val AUTHORS_NBT = "authors"

        fun fromCompoundTag(tag: CompoundTag): ComposerSong? {
            val songString = tag.getString(SONG_NBT)
            if (songString.isEmpty()) return null
            val song = Song.fromString(songString) ?: return null

            val authorsTag = tag.getList(AUTHORS_NBT, Tag.TAG_COMPOUND.toInt())
            val authors: MutableList<Author> = mutableListOf()

            for (authorTag in authorsTag) {
                if (authorTag !is CompoundTag) continue

                val author = Author.fromCompoundTag(authorTag) ?: continue
                authors.add(author)
            }

            return ComposerSong(song, authors)
        }
    }

    constructor() : this(Song(emptyMap()), emptyList())

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

    fun addAuthor(
        player: Player
    ) {
        if (authors.none { it.uuid == player.uuid }) {
            authors = authors + Author(player.uuid, player.gameProfile.name)
        }
    }

    fun addAuthor(
        uuid: UUID,
        name: String
    ) {
        if (authors.none { it.uuid == uuid }) {
            authors = authors + Author(uuid, name)
        }
    }

    fun toTag(): Tag {
        val tag = CompoundTag()
        tag.putString(SONG_NBT, song.toString())

        val authorsListTag = tag.getList(AUTHORS_NBT, Tag.TAG_LIST.toInt())
        for (author in authors) {
            authorsListTag.add(author.toTag())
        }
        tag.put(AUTHORS_NBT, authorsListTag)

        return tag
    }

}