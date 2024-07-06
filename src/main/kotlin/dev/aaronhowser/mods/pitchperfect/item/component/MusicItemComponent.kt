package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class MusicItemComponent(
    val beats: List<Beat>
) {

    companion object {
        val CODEC: Codec<MusicItemComponent> =
            Beat.CODEC.listOf().xmap(::MusicItemComponent, MusicItemComponent::beats)

        val STREAM_CODEC: StreamCodec<ByteBuf, MusicItemComponent> =
            ByteBufCodecs.fromCodec(CODEC)
    }

    data class Beat(
        val sounds: List<Pair<Float, InstrumentComponent.Instrument>>,
        val delayAfter: Int
    ) {

        companion object {
            val CODEC: Codec<Beat> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.pair(
                        Codec.FLOAT,
                        InstrumentComponent.Instrument.CODEC
                    ).listOf().fieldOf("sounds").forGetter(Beat::sounds),
                    Codec.INT.fieldOf("delayAfter").forGetter(Beat::delayAfter)
                ).apply(instance, ::Beat)
            }

            val STREAM_CODEC: StreamCodec<ByteBuf, Beat> =
                ByteBufCodecs.fromCodec(CODEC)

        }

    }

}