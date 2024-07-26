package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

enum class ScreenInstrument(
    val image: ResourceLocation,
    val noteBlockInstrument: NoteBlockInstrument
) {
    BANJO(ScreenTextures.Sprite.Instrument.BANJO, NoteBlockInstrument.BANJO),
    BASS(ScreenTextures.Sprite.Instrument.BASS, NoteBlockInstrument.BASS),
    BASS_DRUM(ScreenTextures.Sprite.Instrument.BASS_DRUM, NoteBlockInstrument.BASEDRUM),
    BIT(ScreenTextures.Sprite.Instrument.BIT, NoteBlockInstrument.BIT),
    CHIMES(ScreenTextures.Sprite.Instrument.CHIMES, NoteBlockInstrument.CHIME),
    COW_BELL(ScreenTextures.Sprite.Instrument.COW_BELL, NoteBlockInstrument.COW_BELL),
    DIDGERIDOO(ScreenTextures.Sprite.Instrument.DIDGERIDOO, NoteBlockInstrument.DIDGERIDOO),
    ELECTRIC_PIANO(ScreenTextures.Sprite.Instrument.ELECTRIC_PIANO, NoteBlockInstrument.PLING),
    FLUTE(ScreenTextures.Sprite.Instrument.FLUTE, NoteBlockInstrument.FLUTE),
    GLOCKENSPIEL(ScreenTextures.Sprite.Instrument.GLOCKENSPIEL, NoteBlockInstrument.BELL),
    GUITAR(ScreenTextures.Sprite.Instrument.GUITAR, NoteBlockInstrument.GUITAR),
    HARP(ScreenTextures.Sprite.Instrument.HARP, NoteBlockInstrument.HARP),
    SNARE_DRUM(ScreenTextures.Sprite.Instrument.SNARE_DRUM, NoteBlockInstrument.SNARE),
    STICKS(ScreenTextures.Sprite.Instrument.STICKS, NoteBlockInstrument.HAT),
    VIBRAPHONE(ScreenTextures.Sprite.Instrument.VIBRAPHONE, NoteBlockInstrument.IRON_XYLOPHONE),
    XYLOPHONE(ScreenTextures.Sprite.Instrument.XYLOPHONE, NoteBlockInstrument.XYLOPHONE)
}