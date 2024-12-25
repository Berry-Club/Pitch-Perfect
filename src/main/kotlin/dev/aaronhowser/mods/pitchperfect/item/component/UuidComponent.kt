package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class UuidComponent(
    val uuid: UUID
) {

    companion object {
        val UUID_CODEC: Codec<UUID> =
            Codec.STRING.xmap(UUID::fromString, UUID::toString)
        val UUID_STREAM_CODEC: StreamCodec<ByteBuf, UUID> =
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString)

        val CODEC: Codec<UuidComponent> =
            UUID_CODEC.xmap(::UuidComponent, UuidComponent::uuid)

        val STREAM_CODEC: StreamCodec<ByteBuf, UuidComponent> =
            UUID_STREAM_CODEC.map(::UuidComponent, UuidComponent::uuid)
    }

}