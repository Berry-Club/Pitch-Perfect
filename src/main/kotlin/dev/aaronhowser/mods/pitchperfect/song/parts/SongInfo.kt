package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class SongInfo(
    val title: String,
    val author: UUID,
    val uuid: UUID,
    val song: Song
) {

    fun toCompoundTag(): CompoundTag {
        val tag = CompoundTag()
        tag.putString("title", title)
        tag.putUUID("author", author)
        tag.putUUID("uuid", uuid)
        tag.putString("song", song.toString())
        return tag
    }

    companion object {
        private val UUID_CODEC: Codec<UUID> =
            Codec.STRING.xmap(UUID::fromString, UUID::toString)
        private val UUID_STREAM_CODEC: StreamCodec<ByteBuf, UUID> =
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString)

        val CODEC: Codec<SongInfo> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("title").forGetter(SongInfo::title),
                    UUID_CODEC.fieldOf("author").forGetter(SongInfo::author),
                    UUID_CODEC.fieldOf("uuid").forGetter(SongInfo::uuid),
                    Song.CODEC.fieldOf("song").forGetter(SongInfo::song)
                ).apply(instance, ::SongInfo)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongInfo> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SongInfo::title,
                UUID_STREAM_CODEC, SongInfo::author,
                UUID_STREAM_CODEC, SongInfo::uuid,
                Song.STREAM_CODEC, SongInfo::song,
                ::SongInfo
            )

        fun fromCompoundTag(tag: CompoundTag): SongInfo {
            val title = tag.getString("title")
            val author = tag.getUUID("author")
            val uuid = tag.getUUID("uuid")
            val song = Song.parse(tag.getString("song"))
            return SongInfo(title, author, uuid, song)
        }
    }

}