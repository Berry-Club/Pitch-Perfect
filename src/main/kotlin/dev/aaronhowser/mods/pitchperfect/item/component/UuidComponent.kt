package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class UuidComponent(
    val uuid: UUID
) {

    companion object {
        val CODEC: Codec<UUID> =
            Codec.STRING.xmap(UUID::fromString, UUID::toString)
        val STREAM_CODEC: StreamCodec<ByteBuf, UUID> =
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString)

        val songUuidComponent: DataComponentType<UUID> by lazy {
            ModDataComponents.SONG_UUID_COMPONENT.get()
        }
    }

}