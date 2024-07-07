package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

data class SongBuilderComponent(
    val startTick: Long,
    val map: Map<Long, Map<NoteBlockInstrument, List<Float>>>
) {

    companion object {

        val CODEC: Codec<SongBuilderComponent> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.LONG.fieldOf("start_tick").forGetter(SongBuilderComponent::startTick),
                    Codec.unboundedMap(
                        Codec.LONG,
                        Codec.unboundedMap(
                            StringRepresentable.fromEnum(NoteBlockInstrument::values),
                            Codec.FLOAT.listOf()
                        )
                    ).fieldOf("map").forGetter(SongBuilderComponent::map)
                ).apply(instance, ::SongBuilderComponent)
            }

        val STREAM_CODEC: StreamCodec<ByteBuf, SongBuilderComponent> =
            ByteBufCodecs.fromCodec(CODEC)

        val component: DataComponentType<SongBuilderComponent> by lazy { ModDataComponents.SONG_BUILDER_COMPONENT.get() }

    }

}