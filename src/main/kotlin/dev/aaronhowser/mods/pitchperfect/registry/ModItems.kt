package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.item.SheetMusicItem
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.DoubleHighBlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(PitchPerfect.ID)

    val BANJO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("banjo") { InstrumentItem(NoteBlockInstrument.BANJO, "a") }
    val BASS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bass") { InstrumentItem(NoteBlockInstrument.BASS, "b") }
    val BASS_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bass_drum") { InstrumentItem(NoteBlockInstrument.BASEDRUM, "c") }
    val BIT: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bit") { InstrumentItem(NoteBlockInstrument.BIT, "d") }
    val CHIMES: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("chimes") { InstrumentItem(NoteBlockInstrument.CHIME, "e") }
    val COW_BELL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("cow_bell") { InstrumentItem(NoteBlockInstrument.COW_BELL, "f") }
    val DIDGERIDOO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("didgeridoo") { InstrumentItem(NoteBlockInstrument.DIDGERIDOO, "g") }
    val ELECTRIC_PIANO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("electric_piano") { InstrumentItem(NoteBlockInstrument.PLING, "h") }
    val FLUTE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("flute") { InstrumentItem(NoteBlockInstrument.FLUTE, "i") }
    val GLOCKENSPIEL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("glockenspiel") { InstrumentItem(NoteBlockInstrument.BELL, "j") }
    val GUITAR: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("guitar") { InstrumentItem(NoteBlockInstrument.GUITAR, "k") }
    val HARP: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("harp") { InstrumentItem(NoteBlockInstrument.HARP, "l") }
    val SNARE_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("snare_drum") { InstrumentItem(NoteBlockInstrument.SNARE, "m") }
    val STICKS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("sticks") { InstrumentItem(NoteBlockInstrument.HAT, "n") }
    val VIBRAPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("vibraphone") { InstrumentItem(NoteBlockInstrument.IRON_XYLOPHONE, "o") }
    val XYLOPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("xylophone") { InstrumentItem(NoteBlockInstrument.XYLOPHONE, "p") }

    val MUSIC_SHEET: DeferredItem<SheetMusicItem> =
        ITEM_REGISTRY.registerItem("sheet_music") { SheetMusicItem() }

    val CONDUCTOR_BOCK_ITEM: DeferredItem<DoubleHighBlockItem> =
        ITEM_REGISTRY.registerItem("conductor") { DoubleHighBlockItem(ModBlocks.CONDUCTOR.get(), Item.Properties()) }

    //TODO: headphones, maybe make it just render on any player who's in the composer gui
    //TODO: Zune

    val instruments = mutableListOf(
        BANJO,
        BASS_DRUM,
        BASS,
        BIT,
        CHIMES,
        COW_BELL,
        DIDGERIDOO,
        ELECTRIC_PIANO,
        FLUTE,
        GLOCKENSPIEL,
        GUITAR,
        HARP,
        SNARE_DRUM,
        STICKS,
        VIBRAPHONE,
        XYLOPHONE
    )

    fun getFromSoundHolder(soundHolder: Holder<SoundEvent>): DeferredItem<InstrumentItem>? {
        return instruments.find { it.get().instrument == soundHolder.value() }
    }

}