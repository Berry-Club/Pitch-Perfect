package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

data class MusicItemComponent(
    val beats: List<Beat>
) {

    companion object {
        val CODEC: Codec<MusicItemComponent> =
            Beat.CODEC.listOf().xmap(::MusicItemComponent, MusicItemComponent::beats)

        val STREAM_CODEC: StreamCodec<ByteBuf, MusicItemComponent> =
            ByteBufCodecs.fromCodec(CODEC)

        val component: DataComponentType<MusicItemComponent> by lazy { ModDataComponents.MUSIC_COMPONENT.get() }
    }

    data class Beat(
        val sounds: List<Doot>,
        val delayAfter: Int
    ) {

        companion object {
            val CODEC: Codec<Beat> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Doot.CODEC.listOf().fieldOf("sounds").forGetter(Beat::sounds),
                    Codec.INT.fieldOf("delay_after").forGetter(Beat::delayAfter)
                ).apply(instance, ::Beat)
            }
        }

        data class Doot(
            val instrument: NoteBlockInstrument,
            val pitch: Float
        ) {

            companion object {
                val CODEC: Codec<Doot> = RecordCodecBuilder.create { instance ->
                    instance.group(
                        InstrumentComponent.INSTRUMENT_CODEC.fieldOf("instrument").forGetter(Doot::instrument),
                        Codec.FLOAT.fieldOf("pitch").forGetter(Doot::pitch)
                    ).apply(instance, ::Doot)
                }
            }

        }

    }

}