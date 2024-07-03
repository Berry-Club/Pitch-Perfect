package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.StringRepresentable.EnumCodec

class InstrumentComponent(
    val instrumentEnum: Instrument
) {

    constructor(instrument: String) : this(Instrument.valueOf(instrument))

    companion object {

        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                EnumCodec.STRING.fieldOf("instrument").forGetter(InstrumentComponent::instrumentEnum)
            ).apply(instance, ::InstrumentComponent)
        }

    }

    enum class Instrument(soundEvent: SoundEvent) {
        BANJO(SoundEvents.NOTE_BLOCK_BANJO.value()),
        BASS_DRUM(SoundEvents.NOTE_BLOCK_BASEDRUM.value()),
        BASS(SoundEvents.NOTE_BLOCK_BASS.value()),
        BIT(SoundEvents.NOTE_BLOCK_BIT.value()),
        CHIMES(SoundEvents.NOTE_BLOCK_CHIME.value()),
        COW_BELL(SoundEvents.NOTE_BLOCK_COW_BELL.value()),
        DIDGERIDOO(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value()),
        ELECTRIC_PIANO(SoundEvents.NOTE_BLOCK_PLING.value()),
        FLUTE(SoundEvents.NOTE_BLOCK_FLUTE.value()),
        GLOCKENSPIEL(SoundEvents.NOTE_BLOCK_BELL.value()),
        GUITAR(SoundEvents.NOTE_BLOCK_GUITAR.value()),
        HARP(SoundEvents.NOTE_BLOCK_HARP.value()),
        SNARE_DRUM(SoundEvents.NOTE_BLOCK_SNARE.value()),
        STICKS(SoundEvents.NOTE_BLOCK_HAT.value()),
        VIBRAPHONE(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value()),
        XYLOPHONE(SoundEvents.NOTE_BLOCK_XYLOPHONE.value())
    }

}