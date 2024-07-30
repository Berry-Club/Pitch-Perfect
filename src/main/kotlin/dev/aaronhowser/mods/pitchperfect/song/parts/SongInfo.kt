package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.player.Player
import java.util.*

data class SongInfo(
    val title: String,
    val authorUuid: UUID,
    val authorName: String,
    val song: Song
) {

    constructor(
        title: String,
        author: Player,
        song: Song
    ) : this(title, author.uuid, author.gameProfile.name, song)

    fun toTag(): Tag {
        val compoundTag = CompoundTag()
        compoundTag.putString(TITLE, title)
        compoundTag.putUUID(AUTHOR_UUID, authorUuid)
        compoundTag.putString(AUTHOR_NAME, authorName)
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
            authorName,
            title,
            uuidComponent,
            songDataComponent,
            playComponent
        )
    }

    companion object {

        private const val TITLE = "title"
        private const val AUTHOR_UUID = "authorUuid"
        private const val AUTHOR_NAME = "authorName"
        private const val SONG = "song"

        val CODEC: Codec<SongInfo> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf(TITLE).forGetter(SongInfo::title),
                    UuidComponent.UUID_CODEC.fieldOf(AUTHOR_UUID).forGetter(SongInfo::authorUuid),
                    Codec.STRING.fieldOf(AUTHOR_NAME).forGetter(SongInfo::authorName),
                    Song.CODEC.fieldOf(SONG).forGetter(SongInfo::song)
                ).apply(instance, ::SongInfo)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongInfo> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SongInfo::title,
                UuidComponent.UUID_STREAM_CODEC, SongInfo::authorUuid,
                ByteBufCodecs.STRING_UTF8, SongInfo::authorName,
                Song.STREAM_CODEC, SongInfo::song,
                ::SongInfo
            )

        fun fromCompoundTag(tag: CompoundTag): SongInfo? {
            val title = tag.getString(TITLE)
            val authorUuid = tag.getUuidOrNull(AUTHOR_UUID) ?: return null
            val authorName = tag.getString(AUTHOR_NAME)
            val song = Song.fromString(tag.getString(SONG))

            if (song == null) {
                PitchPerfect.LOGGER.error("Failed to parse song from tag: $tag")
                return null
            }

            return SongInfo(title, authorUuid, authorName, song)
        }
    }

}