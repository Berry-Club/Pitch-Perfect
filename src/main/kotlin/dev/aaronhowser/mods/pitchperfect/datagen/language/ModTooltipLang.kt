package dev.aaronhowser.mods.pitchperfect.datagen.language

object ModTooltipLang {

	const val JUMP_TO_BEAT = "tooltip.pitchperfect.jump_to_beat"
	const val JUMP_TO_BEAT_SPECIFIC = "tooltip.pitchperfect.jump_to_beat_specific"

	const val COPY = "tooltip.pitchperfect.copy"
	const val PASTE = "tooltip.pitchperfect.paste"
	const val PLAY = "tooltip.pitchperfect.play"
	const val STOP = "tooltip.pitchperfect.stop"

	const val DELAY = "tooltip.pitchperfect.delay"
	const val PITCH = "tooltip.pitchperfect.pitch"
	const val SOUNDS_LIST_START = "tooltip.pitchperfect.sounds_list_start"

	fun add(provider: ModLanguageProvider) {
		provider.add(JUMP_TO_BEAT, "Jump to beat")
		provider.add(JUMP_TO_BEAT_SPECIFIC, "Jump to beat %s")
		provider.add(COPY, "Copy")
		provider.add(PASTE, "Paste")
		provider.add(PLAY, "Play")
		provider.add(STOP, "Stop")
		provider.add(DELAY, "Delay: ")
		provider.add(PITCH, "Pitch: ")
		provider.add(SOUNDS_LIST_START, "Sounds:")
	}

}