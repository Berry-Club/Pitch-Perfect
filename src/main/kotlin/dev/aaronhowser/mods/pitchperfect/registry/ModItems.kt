package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.item.SheetMusicItem
import net.minecraft.world.item.DoubleHighBlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(PitchPerfect.ID)

    val BANJO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("banjo") { InstrumentItem(NoteBlockInstrument.BANJO) }
    val BASS_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bass_drum") { InstrumentItem(NoteBlockInstrument.BASEDRUM) }
    val BASS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bass") { InstrumentItem(NoteBlockInstrument.BASS) }
    val BIT: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bit") { InstrumentItem(NoteBlockInstrument.BIT) }
    val CHIMES: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("chimes") { InstrumentItem(NoteBlockInstrument.CHIME) }
    val COW_BELL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("cow_bell") { InstrumentItem(NoteBlockInstrument.COW_BELL) }
    val DIDGERIDOO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("didgeridoo") { InstrumentItem(NoteBlockInstrument.DIDGERIDOO) }
    val ELECTRIC_PIANO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("electric_piano") { InstrumentItem(NoteBlockInstrument.PLING) }
    val FLUTE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("flute") { InstrumentItem(NoteBlockInstrument.FLUTE) }
    val GLOCKENSPIEL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("glockenspiel") { InstrumentItem(NoteBlockInstrument.BELL) }
    val GUITAR: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("guitar") { InstrumentItem(NoteBlockInstrument.GUITAR) }
    val HARP: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("harp") { InstrumentItem(NoteBlockInstrument.HARP) }
    val SNARE_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("snare_drum") { InstrumentItem(NoteBlockInstrument.SNARE) }
    val STICKS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("sticks") { InstrumentItem(NoteBlockInstrument.HAT) }
    val VIBRAPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("vibraphone") { InstrumentItem(NoteBlockInstrument.IRON_XYLOPHONE) }
    val XYLOPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("xylophone") { InstrumentItem(NoteBlockInstrument.XYLOPHONE) }

    val MUSIC_SHEET: DeferredItem<SheetMusicItem> =
        ITEM_REGISTRY.registerItem("sheet_music") { SheetMusicItem() }

    val CONDUCTOR_BOCK_ITEM: DeferredItem<DoubleHighBlockItem> =
        ITEM_REGISTRY.registerItem("conductor_test") { DoubleHighBlockItem(ModBlocks.CONDUCTOR.get(), Item.Properties()) }

}