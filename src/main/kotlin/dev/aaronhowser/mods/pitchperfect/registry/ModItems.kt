package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(PitchPerfect.ID)

    val BANJO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("banjo", Supplier { InstrumentItem(InstrumentComponent.Instrument.BANJO) })
    val BASS_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("bass_drum", Supplier { InstrumentItem(InstrumentComponent.Instrument.BASEDRUM) })
    val BASS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("bass", Supplier { InstrumentItem(InstrumentComponent.Instrument.BASS) })
    val BIT: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("bit", Supplier { InstrumentItem(InstrumentComponent.Instrument.BIT) })
    val CHIMES: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("chimes", Supplier { InstrumentItem(InstrumentComponent.Instrument.CHIME) })
    val COW_BELL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("cow_bell", Supplier { InstrumentItem(InstrumentComponent.Instrument.COW_BELL) })
    val DIDGERIDOO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("didgeridoo", Supplier { InstrumentItem(InstrumentComponent.Instrument.DIDGERIDOO) })
    val ELECTRIC_PIANO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("electric_piano", Supplier { InstrumentItem(InstrumentComponent.Instrument.PLING) })
    val FLUTE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("flute", Supplier { InstrumentItem(InstrumentComponent.Instrument.FLUTE) })
    val GLOCKENSPIEL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("glockenspiel", Supplier { InstrumentItem(InstrumentComponent.Instrument.BELL) })
    val GUITAR: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("guitar", Supplier { InstrumentItem(InstrumentComponent.Instrument.GUITAR) })
    val HARP: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("harp", Supplier { InstrumentItem(InstrumentComponent.Instrument.HARP) })
    val SNARE_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("snare_drum", Supplier { InstrumentItem(InstrumentComponent.Instrument.SNARE) })
    val STICKS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("sticks", Supplier { InstrumentItem(InstrumentComponent.Instrument.HAT) })
    val VIBRAPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("vibraphone", Supplier { InstrumentItem(InstrumentComponent.Instrument.IRON_XYLOPHONE) })
    val XYLOPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("xylophone", Supplier { InstrumentItem(InstrumentComponent.Instrument.XYLOPHONE) })

}