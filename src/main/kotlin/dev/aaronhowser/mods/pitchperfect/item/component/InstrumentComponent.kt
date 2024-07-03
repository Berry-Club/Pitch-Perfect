package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvents

class InstrumentComponent(
    val instrumentId: Int
) {
    companion object {

        val map = mapOf(
            1 to SoundEvents.NOTE_BLOCK_BANJO.value(),
            2 to SoundEvents.NOTE_BLOCK_BASEDRUM.value(),
            3 to SoundEvents.NOTE_BLOCK_BASS.value(),
            4 to SoundEvents.NOTE_BLOCK_BIT.value(),
            5 to SoundEvents.NOTE_BLOCK_CHIME.value(),
            6 to SoundEvents.NOTE_BLOCK_COW_BELL.value(),
            7 to SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(),
            8 to SoundEvents.NOTE_BLOCK_PLING.value(),
            9 to SoundEvents.NOTE_BLOCK_FLUTE.value(),
            10 to SoundEvents.NOTE_BLOCK_BELL.value(),
            11 to SoundEvents.NOTE_BLOCK_GUITAR.value(),
            12 to SoundEvents.NOTE_BLOCK_HARP.value(),
            13 to SoundEvents.NOTE_BLOCK_SNARE.value(),
            14 to SoundEvents.NOTE_BLOCK_HAT.value(),
            15 to SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(),
            16 to SoundEvents.NOTE_BLOCK_XYLOPHONE.value()
        )

        val CODEC: Codec<InstrumentComponent> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.fieldOf("instrumentId").forGetter(InstrumentComponent::instrumentId)
                ).apply(instance, ::InstrumentComponent)
            }

        val STREAM_CODEC: StreamCodec<ByteBuf, InstrumentComponent> =
            StreamCodec.composite(
                ByteBufCodecs.INT, InstrumentComponent::instrumentId,
                ::InstrumentComponent
            )

        val component: DataComponentType<InstrumentComponent> by lazy {
            ModDataComponents.INSTRUMENT_COMPONENT.get()
        }

    }

}