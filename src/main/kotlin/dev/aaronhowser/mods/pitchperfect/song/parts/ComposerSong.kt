package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import java.util.*

class ComposerSong(
    val uuid: UUID,
    var song: Song,
    val authors: MutableList<Author>
) {

    constructor() : this(UUID.randomUUID(), Song(), mutableListOf())

    companion object {
        val CODEC: Codec<ComposerSong> =
            RecordCodecBuilder.create {
                it.group(
                    UuidComponent.UUID_CODEC
                        .fieldOf("uuid")
                        .forGetter(ComposerSong::uuid),
                    Song.CODEC
                        .fieldOf("song")
                        .forGetter(ComposerSong::song),
                    Author.CODEC.listOf()
                        .fieldOf("authors")
                        .forGetter(ComposerSong::authors)
                ).apply(it, ::ComposerSong)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSong> =
            StreamCodec.composite(
                UuidComponent.UUID_STREAM_CODEC, ComposerSong::uuid,
                Song.STREAM_CODEC, ComposerSong::song,
                Author.STREAM_CODEC.apply(ByteBufCodecs.list()), ComposerSong::authors,
                ::ComposerSong
            )

        private const val SONG_NBT = "song"
        private const val UUID_NBT = "uuid"
        private const val AUTHORS_NBT = "authors"

        fun fromCompoundTag(tag: CompoundTag): ComposerSong? {
            val uuid = tag.getUuidOrNull(UUID_NBT) ?: return null

            val songString = tag.getString(SONG_NBT)
            val song = Song.fromString(songString) ?: return null

            val authors = mutableListOf<Author>()
            val tagAuthors = tag.getList(AUTHORS_NBT, ListTag.TAG_COMPOUND.toInt())
            for (authorTag in tagAuthors) {
                val authorCompoundTag = authorTag as? CompoundTag ?: continue

                val author = Author.fromCompoundTag(authorCompoundTag) ?: continue
                authors.add(author)
            }

            return ComposerSong(uuid, song, authors)
        }
    }

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

        val newSong = song.copy(beats = updatedBeats)
        song = newSong
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

        val newSong = song.copy(beats = updatedBeats)
        song = newSong
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
        addAuthor(player.uuid, player.name.string)
    }

    fun addAuthor(
        uuid: UUID,
        name: String
    ) {
        if (authors.none { it.uuid == uuid }) {
            authors.add(Author(uuid, name))
        }
    }

    fun toTag(): Tag {
        val tag = CompoundTag()

        tag.putUUID(UUID_NBT, uuid)
        tag.putString(SONG_NBT, song.toString())

        val tagAuthors = tag.getList(AUTHORS_NBT, ListTag.TAG_COMPOUND.toInt())

        for (author in authors) {
            tagAuthors.add(author.toTag())
        }

        tag.put(AUTHORS_NBT, tagAuthors)

        return tag
    }

}