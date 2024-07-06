package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.StringRepresentable
import net.minecraft.util.StringRepresentable.EnumCodec

data class InstrumentComponent(
    val instrument: Instrument
) {

    companion object {

        val CODEC: Codec<InstrumentComponent> =
            Instrument.CODEC.xmap(::InstrumentComponent, InstrumentComponent::instrument)

        val STREAM_CODEC: StreamCodec<ByteBuf, InstrumentComponent> =
            Instrument.STREAM_CODEC.map(::InstrumentComponent, InstrumentComponent::instrument)

        val component: DataComponentType<InstrumentComponent> by lazy {
            ModDataComponents.INSTRUMENT_COMPONENT.get()
        }
    }

    enum class Instrument(
        val id: String,
        val soundEvent: Holder<SoundEvent>
    ) : StringRepresentable {
        BANJO("banjo", SoundEvents.NOTE_BLOCK_BANJO),
        BASS_DRUM("bass_drum", SoundEvents.NOTE_BLOCK_BASEDRUM),
        BASS("bass", SoundEvents.NOTE_BLOCK_BASS),
        BIT("bit", SoundEvents.NOTE_BLOCK_BIT),
        CHIMES("chimes", SoundEvents.NOTE_BLOCK_CHIME),
        COW_BELL("cow_bell", SoundEvents.NOTE_BLOCK_COW_BELL),
        DIDGERIDOO("didgeridoo", SoundEvents.NOTE_BLOCK_DIDGERIDOO),
        ELECTRIC_PIANO("electric_piano", SoundEvents.NOTE_BLOCK_PLING),
        FLUTE("flute", SoundEvents.NOTE_BLOCK_FLUTE),
        GLOCKENSPIEL("glockenspiel", SoundEvents.NOTE_BLOCK_BELL),
        GUITAR("guitar", SoundEvents.NOTE_BLOCK_GUITAR),
        HARP("harp", SoundEvents.NOTE_BLOCK_HARP),
        SNARE_DRUM("snare_drum", SoundEvents.NOTE_BLOCK_SNARE),
        STICKS("sticks", SoundEvents.NOTE_BLOCK_HAT),
        VIBRAPHONE("vibraphone", SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE),
        XYLOPHONE("xylophone", SoundEvents.NOTE_BLOCK_XYLOPHONE),

        ;

        override fun getSerializedName(): String {
            return this.id
        }

        companion object {
            val CODEC: EnumCodec<Instrument> = StringRepresentable.fromEnum(Instrument::values)

            val STREAM_CODEC: StreamCodec<ByteBuf, Instrument> = ByteBufCodecs.fromCodec(CODEC)
        }
    }

}