package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class BooleanComponent(
    val value: Boolean
) {

    companion object {
        val CODEC: Codec<BooleanComponent> =
            Codec.BOOL.xmap(::BooleanComponent, BooleanComponent::value)

        val STREAM_CODEC: StreamCodec<ByteBuf, BooleanComponent> =
            ByteBufCodecs.BOOL.map(::BooleanComponent, BooleanComponent::value)

        val BooleanComponent?.isTrue
            get() = this?.value == true

    }
}