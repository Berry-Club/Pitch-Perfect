package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.player.Player
import java.util.*

data class Author(
    val uuid: UUID,
    val name: String
) {

    constructor(player: Player) : this(player.uuid, player.gameProfile.name)

    fun toTag(): Tag {
        val tag = CompoundTag()
        tag.putUUID(UUID, uuid)
        tag.putString(NAME, name)
        return tag
    }

    companion object {
        val CODEC: Codec<Author> =
            RecordCodecBuilder.create {
                it.group(
                    UuidComponent.UUID_CODEC.fieldOf(UUID).forGetter(Author::uuid),
                    Codec.STRING.fieldOf(NAME).forGetter(Author::name)
                ).apply(it, ::Author)
            }

        val STREAM_CODEC: StreamCodec<ByteBuf, Author> =
            StreamCodec.composite(
                UuidComponent.UUID_STREAM_CODEC, Author::uuid,
                ByteBufCodecs.STRING_UTF8, Author::name,
                ::Author
            )

        const val UUID = "uuid"
        const val NAME = "name"

        fun fromCompoundTag(tag: CompoundTag): Author? {
            val uuid = tag.getUuidOrNull(UUID) ?: return null
            val name = tag.getString(NAME)
            return Author(uuid, name)
        }

    }

}