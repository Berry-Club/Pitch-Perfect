package dev.aaronhowser.mods.pitchperfect.datagen.language

object ModAdvancementLang {

	const val ROOT_DESC = "advancement.pitchperfect.root.desc"

	const val HIT_MOB_TITLE = "advancement.pitchperfect.hit_mob.title"
	const val HIT_MOB_DESC = "advancement.pitchperfect.hit_mob.desc"

	const val MAKE_COMPOSER_TITLE = "advancement.pitchperfect.make_composer.title"
	const val MAKE_COMPOSER_DESC = "advancement.pitchperfect.make_composer.desc"

	const val MAKE_CONDUCTOR_TITLE = "advancement.pitchperfect.make_conductor.title"
	const val MAKE_CONDUCTOR_DESC = "advancement.pitchperfect.make_conductor.desc"

	const val CONDUCTOR_COMPLEX_TITLE = "advancement.pitchperfect.conductor_complex.title"
	const val CONDUCTOR_COMPLEX_DESC = "advancement.pitchperfect.conductor_complex.desc"

	const val ENCHANT_INSTRUMENT_TITLE = "advancement.pitchperfect.enchant_instrument.title"
	const val ENCHANT_INSTRUMENT_DESC = "advancement.pitchperfect.enchant_instrument.desc"

	const val AND_HIS_MUSIC_TITLE = "advancement.pitchperfect.and_his_music.title"
	const val AND_HIS_MUSIC_DESC = "advancement.pitchperfect.and_his_music.desc"

	const val HEALING_BEAT_TITLE = "advancement.pitchperfect.healing_beat.title"
	const val HEALING_BEAT_DESC = "advancement.pitchperfect.healing_beat.desc"

	const val BWAAAP_TITLE = "advancement.pitchperfect.bwaaap.title"
	const val BWAAAP_DESC = "advancement.pitchperfect.bwaaap.desc"

	fun add(provider: ModLanguageProvider) {
		provider.add(ROOT_DESC, "Get any instrument!\n\nRight-click to play a note, depending on the angle you're looking!")
		provider.add(HIT_MOB_TITLE, "Hit the Right Note")
		provider.add(HIT_MOB_DESC, "Hit a mob using an instrument")
		provider.add(MAKE_COMPOSER_TITLE, "Maestro")
		provider.add(MAKE_COMPOSER_DESC, "Make a Composer\n\nThis acts as a simple digital audio workstation to make songs!")
		provider.add(MAKE_CONDUCTOR_TITLE, "Ghostly Symphony")
		provider.add(MAKE_CONDUCTOR_DESC, "Make a Conductor\n\nPlace Armor Stands nearby and give them Instruments, put Sheet Music in the Conductor, then give it a redstone signal to play the song!")
		provider.add(CONDUCTOR_COMPLEX_TITLE, "Polyphony")
		provider.add(CONDUCTOR_COMPLEX_DESC, "Play a song in the Conductor that has at least 3 different instruments")
		provider.add(ENCHANT_INSTRUMENT_TITLE, "The Magic of Music")
		provider.add(ENCHANT_INSTRUMENT_DESC, "Enchant any instrument")
		provider.add(AND_HIS_MUSIC_TITLE, "And His Music Was Electric")
		provider.add(AND_HIS_MUSIC_DESC, "Activate the enchantment And His Music Was Electric\n\nWhen anywhere in your inventory, dealing damage will chain to other mobs, but damage the instrument")
		provider.add(HEALING_BEAT_TITLE, "Music Therapy")
		provider.add(HEALING_BEAT_DESC, "Heal something using the Healing Beat enchantment")
		provider.add(BWAAAP_TITLE, "BWAAAAAAAP")
		provider.add(BWAAAP_DESC, "Knock back entities with the BWAAAP enchantment")
	}


}