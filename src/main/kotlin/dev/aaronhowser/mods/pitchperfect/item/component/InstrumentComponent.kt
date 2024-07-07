package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

data class InstrumentComponent(
    val instrument: NoteBlockInstrument
) {

    companion object {

        val INSTRUMENT_CODEC: StringRepresentable.EnumCodec<NoteBlockInstrument> =
            StringRepresentable.fromEnum(NoteBlockInstrument::values)

        val CODEC: Codec<InstrumentComponent> =
            INSTRUMENT_CODEC.xmap(::InstrumentComponent, InstrumentComponent::instrument)

        val STREAM_CODEC: StreamCodec<ByteBuf, InstrumentComponent> =
            ByteBufCodecs.fromCodec(CODEC)

        val component: DataComponentType<InstrumentComponent> by lazy {
            ModDataComponents.INSTRUMENT_COMPONENT.get()
        }
    }

}