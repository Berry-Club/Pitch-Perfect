package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import net.minecraft.nbt.CompoundTag
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

    val uuid: UUID = UUID.nameUUIDFromBytes(song.toString().toByteArray())

    fun toCompoundTag(): CompoundTag {
        val tag = CompoundTag()
        tag.putString(TITLE, title)
        tag.putUUID(AUTHOR_UUID, authorUuid)
        tag.putString(AUTHOR_NAME, authorName)
        tag.putString(SONG, song.toString())
        return tag
    }

    fun getComponent(): Component {
        val component = Component.literal("$authorName - $title")

        component.withStyle {
            it
                .withHoverEvent(
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        Component.literal("Click to copy raw song data.\n${song}")
                    )
                )
                .withClickEvent(
                    ClickEvent(
                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                        song.toString()
                    )
                )
        }

        return component
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
            try {
                val title = tag.getString(TITLE)
                val authorUuid = tag.getUUID(AUTHOR_UUID)
                val authorName = tag.getString(AUTHOR_NAME)
                val song = Song.parse(tag.getString(SONG))

                return SongInfo(title, authorUuid, authorName, song)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }

}