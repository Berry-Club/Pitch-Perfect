package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import net.minecraft.sounds.SoundEvents
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(PitchPerfect.ID)

    val BANJO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("banjo", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_BANJO) })
    val BASS_DRUM: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("bass_drum", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_BASEDRUM) })
    val BASS: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("bass", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_BASS) })
    val BIT: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("bit", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_BIT) })
    val CHIMES: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("chimes", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_CHIME) })
    val COW_BELL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("cow_bell", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_COW_BELL) })
    val DIDGERIDOO: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("didgeridoo", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_DIDGERIDOO) })
    val PLING: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("electric_piano", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_PLING) })
    val FLUTE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("flute", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_FLUTE) })
    val BELL: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("glockenspiel", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_BELL) })
    val GUITAR: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("guitar", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_GUITAR) })
    val HARP: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("harp", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_HARP) })
    val SNARE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("snare_drum", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_SNARE) })
    val HAT: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("sticks", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_HAT) })
    val IRON_XYLOPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("vibraphone", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE) })
    val XYLOPHONE: DeferredItem<InstrumentItem> =
        ITEM_REGISTRY.register("xylophone", Supplier { InstrumentItem(SoundEvents.NOTE_BLOCK_XYLOPHONE) })

}