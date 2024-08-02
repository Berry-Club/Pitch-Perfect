package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.player.Player

data class SongInfo(
    val title: String,
    val authors: List<Author>,
    val song: Song
) {

    constructor(
        title: String,
        player: Player,
        song: Song
    ) : this(title, listOf(Author(player)), song)

    fun toTag(): Tag {
        val compoundTag = CompoundTag()
        compoundTag.putString(TITLE, title)

        val tagAuthors = compoundTag.getList(AUTHORS, ListTag.TAG_COMPOUND.toInt())

        for (author in authors) {
            val authorTag = CompoundTag()
            authorTag.putUUID(AUTHOR_UUID, author.uuid)
            authorTag.putString(AUTHOR_NAME, author.name)
            tagAuthors.add(authorTag)
        }

        compoundTag.put(AUTHORS, tagAuthors)

        compoundTag.putString(SONG, song.toString())

        return compoundTag
    }

    fun getComponent(): Component {
        val uuidComponent = ModLanguageProvider.Misc.SONG_UUID.toComponent()
            .withStyle {
                it
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            ModLanguageProvider.Message.CLICK_COPY_SONG_UUID.toComponent(song.uuid.toString())
                        )
                    )
                    .withClickEvent(
                        ClickEvent(
                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                            song.uuid.toString()
                        )
                    )
            }

        val authorsHoverComponent = Component.empty()
        for (author in authors) {
            authorsHoverComponent.append(Component.literal(author.name))
        }

        val authorsComponent = ModLanguageProvider.Misc.SONG_AUTHORS.toComponent()
            .withStyle {
                it.withHoverEvent(
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        authorsHoverComponent
                    )
                )
            }

        val songDataComponent = ModLanguageProvider.Misc.SONG_RAW.toComponent()
            .withStyle {
                it
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            ModLanguageProvider.Message.CLICK_COPY_RAW_SONG.toComponent(song.toString())
                        )
                    )
                    .withClickEvent(
                        ClickEvent(
                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                            song.toString()
                        )
                    )
            }

        val playComponent = ModLanguageProvider.Misc.SONG_PLAY.toComponent()
            .withStyle {
                it
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            ModLanguageProvider.Message.CLICK_PLAY_SONG.toComponent()
                        )
                    )
                    .withClickEvent(
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/pitchperfect playSong ${song.uuid}"
                        )
                    )
            }

        return ModLanguageProvider.Misc.SONG_INFO.toComponent(
            title,
            authorsComponent,
            uuidComponent,
            songDataComponent,
            playComponent,
        )
    }

    companion object {

        private const val TITLE = "title"
        private const val AUTHORS = "authors"
        private const val AUTHOR_UUID = "uuid"
        private const val AUTHOR_NAME = "name"
        private const val SONG = "song"

        val CODEC: Codec<SongInfo> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf(TITLE).forGetter(SongInfo::title),
                    Author.CODEC.listOf().fieldOf(AUTHORS).forGetter(SongInfo::authors),
                    Song.CODEC.fieldOf(SONG).forGetter(SongInfo::song)
                ).apply(instance, ::SongInfo)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongInfo> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SongInfo::title,
                Author.STREAM_CODEC.apply(ByteBufCodecs.list()), SongInfo::authors,
                Song.STREAM_CODEC, SongInfo::song,
                ::SongInfo
            )

        fun fromCompoundTag(tag: CompoundTag): SongInfo? {
            val title = tag.getString(TITLE)

            val authors = mutableListOf<Author>()
            val tagAuthors = tag.getList(AUTHORS, ListTag.TAG_COMPOUND.toInt())
            for (authorTag in tagAuthors) {
                val authorCompoundTag = tag as? CompoundTag ?: continue

                val uuid = authorCompoundTag.getUuidOrNull(AUTHOR_UUID) ?: continue
                val name = authorCompoundTag.getString(AUTHOR_NAME)

                authors.add(Author(uuid, name))
            }

            val song = Song.fromString(tag.getString(SONG))

            if (song == null) {
                PitchPerfect.LOGGER.error("Failed to parse song from tag: $tag")
                return null
            }

            return SongInfo(title, authors, song)
        }
    }

}