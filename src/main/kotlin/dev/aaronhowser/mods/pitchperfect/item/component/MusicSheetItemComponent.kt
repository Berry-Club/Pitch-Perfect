package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.util.function.Function

data class MusicSheetItemComponent(
    val beats: List<SoundsWithDelayAfter>
) {

    data class SoundsWithDelayAfter(
        val sounds: Map<NoteBlockInstrument, List<Float>>,
        val delayAfter: Int
    ) {

        companion object {
            val PITCH_LIST_CODEC: Codec<List<Float>> = Codec.either(
                Codec.FLOAT,
                Codec.FLOAT.listOf()
            ).xmap(
                { a: Either<Float, List<Float>> -> a.map(::listOf, Function.identity()) },
                { list: List<Float> -> if (list.size == 1) Either.left(list.first()) else Either.right(list) }
            )

            val CODEC: Codec<SoundsWithDelayAfter> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.unboundedMap(
                        StringRepresentable.fromEnum(NoteBlockInstrument::values),
                        PITCH_LIST_CODEC
                    ).fieldOf("sounds").forGetter(SoundsWithDelayAfter::sounds),
                    Codec.INT.optionalFieldOf("delay_after", 1).forGetter(SoundsWithDelayAfter::delayAfter)
                ).apply(instance, ::SoundsWithDelayAfter)
            }

        }
    }

    companion object {

        val CODEC: Codec<MusicSheetItemComponent> =
            SoundsWithDelayAfter.CODEC.listOf().xmap(::MusicSheetItemComponent, MusicSheetItemComponent::beats)

        val STREAM_CODEC: StreamCodec<ByteBuf, MusicSheetItemComponent> = ByteBufCodecs.fromCodec(CODEC)

        val component: DataComponentType<MusicSheetItemComponent> by lazy {
            ModDataComponents.MUSIC_SHEET_COMPONENT.get()
        }

    }

}