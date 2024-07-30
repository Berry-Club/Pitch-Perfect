package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

enum class ScreenInstrument(
    val image: ResourceLocation,
    val noteBlockInstrument: NoteBlockInstrument,
    val displayName: Component
) {
    BANJO(
        ScreenTextures.Sprite.Instrument.BANJO,
        NoteBlockInstrument.BANJO,
        ModLanguageProvider.Item.BANJO.toComponent()
    ),
    BASS(
        ScreenTextures.Sprite.Instrument.BASS,
        NoteBlockInstrument.BASS,
        ModLanguageProvider.Item.BASS.toComponent()
    ),
    BASS_DRUM(
        ScreenTextures.Sprite.Instrument.BASS_DRUM,
        NoteBlockInstrument.BASEDRUM,
        ModLanguageProvider.Item.BASS_DRUM.toComponent()
    ),
    BIT(
        ScreenTextures.Sprite.Instrument.BIT,
        NoteBlockInstrument.BIT,
        ModLanguageProvider.Item.BIT.toComponent()
    ),
    CHIMES(
        ScreenTextures.Sprite.Instrument.CHIMES,
        NoteBlockInstrument.CHIME,
        ModLanguageProvider.Item.CHIMES.toComponent()
    ),
    COW_BELL(
        ScreenTextures.Sprite.Instrument.COW_BELL,
        NoteBlockInstrument.COW_BELL,
        ModLanguageProvider.Item.COW_BELL.toComponent()
    ),
    DIDGERIDOO(
        ScreenTextures.Sprite.Instrument.DIDGERIDOO,
        NoteBlockInstrument.DIDGERIDOO,
        ModLanguageProvider.Item.DIDGERIDOO.toComponent()
    ),
    ELECTRIC_PIANO(
        ScreenTextures.Sprite.Instrument.ELECTRIC_PIANO,
        NoteBlockInstrument.PLING,
        ModLanguageProvider.Item.ELECTRIC_PIANO.toComponent()
    ),
    FLUTE(
        ScreenTextures.Sprite.Instrument.FLUTE,
        NoteBlockInstrument.FLUTE,
        ModLanguageProvider.Item.FLUTE.toComponent()
    ),
    GLOCKENSPIEL(
        ScreenTextures.Sprite.Instrument.GLOCKENSPIEL,
        NoteBlockInstrument.BELL,
        ModLanguageProvider.Item.GLOCKENSPIEL.toComponent()
    ),
    GUITAR(
        ScreenTextures.Sprite.Instrument.GUITAR,
        NoteBlockInstrument.GUITAR,
        ModLanguageProvider.Item.GUITAR.toComponent()
    ),
    HARP(
        ScreenTextures.Sprite.Instrument.HARP,
        NoteBlockInstrument.HARP,
        ModLanguageProvider.Item.HARP.toComponent()
    ),
    SNARE_DRUM(
        ScreenTextures.Sprite.Instrument.SNARE_DRUM,
        NoteBlockInstrument.SNARE,
        ModLanguageProvider.Item.SNARE_DRUM.toComponent()
    ),
    STICKS(
        ScreenTextures.Sprite.Instrument.STICKS,
        NoteBlockInstrument.HAT,
        ModLanguageProvider.Item.STICKS.toComponent()
    ),
    VIBRAPHONE(
        ScreenTextures.Sprite.Instrument.VIBRAPHONE,
        NoteBlockInstrument.IRON_XYLOPHONE,
        ModLanguageProvider.Item.VIBRAPHONE.toComponent()
    ),
    XYLOPHONE(
        ScreenTextures.Sprite.Instrument.XYLOPHONE,
        NoteBlockInstrument.XYLOPHONE,
        ModLanguageProvider.Item.XYLOPHONE.toComponent()
    );
}