package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import net.minecraft.resources.ResourceLocation

enum class ScreenInstrument(
    val image: ResourceLocation,
    val instrumentItem: InstrumentItem
) {
    BANJO(ScreenTextures.Sprite.Instrument.BANJO, ModItems.BANJO.get()),
    BASS(ScreenTextures.Sprite.Instrument.BASS, ModItems.BASS.get()),
    BASS_DRUM(ScreenTextures.Sprite.Instrument.BASS_DRUM, ModItems.BASS_DRUM.get()),
    BIT(ScreenTextures.Sprite.Instrument.BIT, ModItems.BIT.get()),
    CHIMES(ScreenTextures.Sprite.Instrument.CHIMES, ModItems.CHIMES.get()),
    COW_BELL(ScreenTextures.Sprite.Instrument.COW_BELL, ModItems.COW_BELL.get()),
    DIDGERIDOO(ScreenTextures.Sprite.Instrument.DIDGERIDOO, ModItems.DIDGERIDOO.get()),
    ELECTRIC_PIANO(ScreenTextures.Sprite.Instrument.ELECTRIC_PIANO, ModItems.ELECTRIC_PIANO.get()),
    FLUTE(ScreenTextures.Sprite.Instrument.FLUTE, ModItems.FLUTE.get()),
    GLOCKENSPIEL(ScreenTextures.Sprite.Instrument.GLOCKENSPIEL, ModItems.GLOCKENSPIEL.get()),
    GUITAR(ScreenTextures.Sprite.Instrument.GUITAR, ModItems.GUITAR.get()),
    HARP(ScreenTextures.Sprite.Instrument.HARP, ModItems.HARP.get()),
    SNARE_DRUM(ScreenTextures.Sprite.Instrument.SNARE_DRUM, ModItems.SNARE_DRUM.get()),
    STICKS(ScreenTextures.Sprite.Instrument.STICKS, ModItems.STICKS.get()),
    VIBRAPHONE(ScreenTextures.Sprite.Instrument.VIBRAPHONE, ModItems.VIBRAPHONE.get()),
    XYLOPHONE(ScreenTextures.Sprite.Instrument.XYLOPHONE, ModItems.XYLOPHONE.get())
}