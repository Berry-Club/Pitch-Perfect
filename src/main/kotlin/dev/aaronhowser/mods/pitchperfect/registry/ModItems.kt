package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(PitchPerfect.ID)

    val BANJO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("banjo") { InstrumentItem(InstrumentComponent.Instrument.BANJO) }
    val BASS_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bass_drum") { InstrumentItem(InstrumentComponent.Instrument.BASEDRUM) }
    val BASS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bass") { InstrumentItem(InstrumentComponent.Instrument.BASS) }
    val BIT: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("bit") { InstrumentItem(InstrumentComponent.Instrument.BIT) }
    val CHIMES: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("chimes") { InstrumentItem(InstrumentComponent.Instrument.CHIME) }
    val COW_BELL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("cow_bell") { InstrumentItem(InstrumentComponent.Instrument.COW_BELL) }
    val DIDGERIDOO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("didgeridoo") { InstrumentItem(InstrumentComponent.Instrument.DIDGERIDOO) }
    val ELECTRIC_PIANO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("electric_piano") { InstrumentItem(InstrumentComponent.Instrument.PLING) }
    val FLUTE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("flute") { InstrumentItem(InstrumentComponent.Instrument.FLUTE) }
    val GLOCKENSPIEL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("glockenspiel") { InstrumentItem(InstrumentComponent.Instrument.BELL) }
    val GUITAR: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("guitar") { InstrumentItem(InstrumentComponent.Instrument.GUITAR) }
    val HARP: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("harp") { InstrumentItem(InstrumentComponent.Instrument.HARP) }
    val SNARE_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("snare_drum") { InstrumentItem(InstrumentComponent.Instrument.SNARE) }
    val STICKS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("sticks") { InstrumentItem(InstrumentComponent.Instrument.HAT) }
    val VIBRAPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("vibraphone") { InstrumentItem(InstrumentComponent.Instrument.IRON_XYLOPHONE) }
    val XYLOPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.registerItem("xylophone") { InstrumentItem(InstrumentComponent.Instrument.XYLOPHONE) }

}