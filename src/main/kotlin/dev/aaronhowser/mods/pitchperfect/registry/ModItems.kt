package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider
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
		registerInstrument("banjo", NoteBlockInstrument.BANJO, ModLanguageProvider.FontIcon.BANJO)
	val BASS: DeferredItem<InstrumentItem> =
		registerInstrument("bass", NoteBlockInstrument.BASS, ModLanguageProvider.FontIcon.BASS)
	val BASS_DRUM: DeferredItem<InstrumentItem> =
		registerInstrument("bass_drum", NoteBlockInstrument.BASEDRUM, ModLanguageProvider.FontIcon.BASS_DRUM)
	val BIT: DeferredItem<InstrumentItem> =
		registerInstrument("bit", NoteBlockInstrument.BIT, ModLanguageProvider.FontIcon.BIT)
	val CHIMES: DeferredItem<InstrumentItem> =
		registerInstrument("chimes", NoteBlockInstrument.CHIME, ModLanguageProvider.FontIcon.CHIMES)
	val COW_BELL: DeferredItem<InstrumentItem> =
		registerInstrument("cow_bell", NoteBlockInstrument.COW_BELL, ModLanguageProvider.FontIcon.COW_BELL)
	val DIDGERIDOO: DeferredItem<InstrumentItem> =
		registerInstrument("didgeridoo", NoteBlockInstrument.DIDGERIDOO, ModLanguageProvider.FontIcon.DIDGERIDOO)
	val ELECTRIC_PIANO: DeferredItem<InstrumentItem> =
		registerInstrument("electric_piano", NoteBlockInstrument.PLING, ModLanguageProvider.FontIcon.ELECTRIC_PIANO)
	val FLUTE: DeferredItem<InstrumentItem> =
		registerInstrument("flute", NoteBlockInstrument.FLUTE, ModLanguageProvider.FontIcon.FLUTE)
	val GLOCKENSPIEL: DeferredItem<InstrumentItem> =
		registerInstrument("glockenspiel", NoteBlockInstrument.BELL, ModLanguageProvider.FontIcon.GLOCKENSPIEL)
	val GUITAR: DeferredItem<InstrumentItem> =
		registerInstrument("guitar", NoteBlockInstrument.GUITAR, ModLanguageProvider.FontIcon.GUITAR)
	val HARP: DeferredItem<InstrumentItem> =
		registerInstrument("harp", NoteBlockInstrument.HARP, ModLanguageProvider.FontIcon.HARP)
	val SNARE_DRUM: DeferredItem<InstrumentItem> =
		registerInstrument("snare_drum", NoteBlockInstrument.SNARE, ModLanguageProvider.FontIcon.SNARE_DRUM)
	val STICKS: DeferredItem<InstrumentItem> =
		registerInstrument("sticks", NoteBlockInstrument.HAT, ModLanguageProvider.FontIcon.STICKS)
	val VIBRAPHONE: DeferredItem<InstrumentItem> =
		registerInstrument("vibraphone", NoteBlockInstrument.IRON_XYLOPHONE, ModLanguageProvider.FontIcon.VIBRAPHONE)
	val XYLOPHONE: DeferredItem<InstrumentItem> =
		registerInstrument("xylophone", NoteBlockInstrument.XYLOPHONE, ModLanguageProvider.FontIcon.XYLOPHONE)

	val SHEET_MUSIC: DeferredItem<SheetMusicItem> =
		register("sheet_music", ::SheetMusicItem)

	@Suppress("unused")
	val CONDUCTOR_BLOCK_ITEM: DeferredItem<DoubleHighBlockItem> =
		register("conductor") { DoubleHighBlockItem(ModBlocks.CONDUCTOR.get(), Item.Properties()) }

	//TODO: headphones, maybe make it just render on any player who's in the composer gui
	//TODO: Zune

	fun getFromSoundHolder(soundHolder: Holder<SoundEvent>): DeferredItem<InstrumentItem>? {
		val instruments = ITEM_REGISTRY.entries.filterIsInstance<DeferredItem<InstrumentItem>>()
		return instruments.find { it.get().instrument == soundHolder.value() }
	}

	fun <T : Item> register(
		name: String,
		item: () -> T
	): DeferredItem<T> {
		return ITEM_REGISTRY.registerItem(name) { item() }
	}

	fun registerInstrument(
		name: String,
		instrument: NoteBlockInstrument,
		fontChar: Char
	): DeferredItem<InstrumentItem> {
		return register(name) { InstrumentItem(instrument, fontChar) }
	}


}