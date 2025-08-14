package dev.aaronhowser.mods.pitchperfect.datagen.language

import dev.aaronhowser.mods.pitchperfect.registry.ModItems

object ModItemLang {
	fun add(provider: ModLanguageProvider) {
		provider.addItem(ModItems.BASS, "Bass")
		provider.addItem(ModItems.BASS_DRUM, "Bass Drum")
		provider.addItem(ModItems.BANJO, "Banjo")
		provider.addItem(ModItems.BIT, "Bit Player")
		provider.addItem(ModItems.CHIMES, "Chimes")
		provider.addItem(ModItems.COW_BELL, "Cow Bell")
		provider.addItem(ModItems.DIDGERIDOO, "Didgeridoo")
		provider.addItem(ModItems.ELECTRIC_PIANO, "Electric Piano")
		provider.addItem(ModItems.FLUTE, "Flute")
		provider.addItem(ModItems.GLOCKENSPIEL, "Glockenspiel")
		provider.addItem(ModItems.GUITAR, "Guitar")
		provider.addItem(ModItems.HARP, "Harp")
		provider.addItem(ModItems.SNARE_DRUM, "Snare Drum")
		provider.addItem(ModItems.STICKS, "Drum Sticks")
		provider.addItem(ModItems.VIBRAPHONE, "Vibraphone")
		provider.addItem(ModItems.XYLOPHONE, "Xylophone")
		provider.addItem(ModItems.SHEET_MUSIC, "Sheet Music")
	}
}