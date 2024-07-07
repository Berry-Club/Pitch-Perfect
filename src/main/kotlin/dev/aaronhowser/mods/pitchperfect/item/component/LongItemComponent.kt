package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class LongItemComponent(
    val long: Long
) {

    companion object {

        val CODEC: Codec<LongItemComponent> = Codec.LONG.xmap(::LongItemComponent, LongItemComponent::long)

        val STREAM_CODEC: StreamCodec<ByteBuf, LongItemComponent> =
            ByteBufCodecs.VAR_LONG.map(::LongItemComponent, LongItemComponent::long)

        val startedRecordingAt: DataComponentType<LongItemComponent> by lazy { ModDataComponents.STARTED_RECORDING_AT_COMPONENT.get() }

    }

}