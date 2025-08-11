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
		registerInstrument("banjo", NoteBlockInstrument.BANJO, "a")
	val BASS: DeferredItem<InstrumentItem> =
		registerInstrument("bass", NoteBlockInstrument.BASS, "b")
	val BASS_DRUM: DeferredItem<InstrumentItem> =
		registerInstrument("bass_drum", NoteBlockInstrument.BASEDRUM, "c")
	val BIT: DeferredItem<InstrumentItem> =
		registerInstrument("bit", NoteBlockInstrument.BIT, "d")
	val CHIMES: DeferredItem<InstrumentItem> =
		registerInstrument("chimes", NoteBlockInstrument.CHIME, "e")
	val COW_BELL: DeferredItem<InstrumentItem> =
		registerInstrument("cow_bell", NoteBlockInstrument.COW_BELL, "f")
	val DIDGERIDOO: DeferredItem<InstrumentItem> =
		registerInstrument("didgeridoo", NoteBlockInstrument.DIDGERIDOO, "g")
	val ELECTRIC_PIANO: DeferredItem<InstrumentItem> =
		registerInstrument("electric_piano", NoteBlockInstrument.PLING, "h")
	val FLUTE: DeferredItem<InstrumentItem> =
		registerInstrument("flute", NoteBlockInstrument.FLUTE, "i")
	val GLOCKENSPIEL: DeferredItem<InstrumentItem> =
		registerInstrument("glockenspiel", NoteBlockInstrument.BELL, "j")
	val GUITAR: DeferredItem<InstrumentItem> =
		registerInstrument("guitar", NoteBlockInstrument.GUITAR, "k")
	val HARP: DeferredItem<InstrumentItem> =
		registerInstrument("harp", NoteBlockInstrument.HARP, "l")
	val SNARE_DRUM: DeferredItem<InstrumentItem> =
		registerInstrument("snare_drum", NoteBlockInstrument.SNARE, "m")
	val STICKS: DeferredItem<InstrumentItem> =
		registerInstrument("sticks", NoteBlockInstrument.HAT, "n")
	val VIBRAPHONE: DeferredItem<InstrumentItem> =
		registerInstrument("vibraphone", NoteBlockInstrument.IRON_XYLOPHONE, "o")
	val XYLOPHONE: DeferredItem<InstrumentItem> =
		registerInstrument("xylophone", NoteBlockInstrument.XYLOPHONE, "p")

	val MUSIC_SHEET: DeferredItem<SheetMusicItem> =
		ITEM_REGISTRY.registerItem("sheet_music") { SheetMusicItem() }

	val CONDUCTOR_BOCK_ITEM: DeferredItem<DoubleHighBlockItem> =
		ITEM_REGISTRY.registerItem("conductor") { DoubleHighBlockItem(ModBlocks.CONDUCTOR.get(), Item.Properties()) }

	//TODO: headphones, maybe make it just render on any player who's in the composer gui
	//TODO: Zune

	fun getFromSoundHolder(soundHolder: Holder<SoundEvent>): DeferredItem<InstrumentItem>? {
		val instruments = ITEM_REGISTRY.entries.filterIsInstance<DeferredItem<InstrumentItem>>()
		return instruments.find { it.get().instrument == soundHolder.value() }
	}

	fun registerInstrument(
		name: String,
		instrument: NoteBlockInstrument,
		fontString: String
	): DeferredItem<InstrumentItem> {
		return ITEM_REGISTRY.registerItem(name) { InstrumentItem(instrument, fontString) }
	}


}