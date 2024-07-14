package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
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

        val isRecordingComponent: DataComponentType<BooleanComponent> by lazy { ModDataComponents.IS_RECORDING_COMPONENT.get() }

        val BooleanComponent?.isTrue
            get() = this?.value == true

    }
}