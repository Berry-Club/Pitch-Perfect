package dev.aaronhowser.mods.pitchperfect.datagen.language

object ModMessageLang {
	const val INSTRUMENT_BROKEN = "subtitle.pitchperfect.instrument_broken"
	const val SONGS_LIST = "message.pitchperfect.songs_list"
	const val SONG_PASTE_ADDED = "message.pitchperfect.song_paste_added"
	const val CLICK_COPY_SONG_UUID = "message.pitchperfect.click_copy_song_uuid"
	const val CLICK_COPY_RAW_SONG = "message.pitchperfect.click_copy_raw_song"
	const val CLICK_PLAY_SONG = "message.pitchperfect.song_play"
	const val SONG_COPIED = "message.pitchperfect.song_copied"
	const val SONG_PASTED = "message.pitchperfect.song_pasted"
	const val SONG_PASTE_FAIL_DUPLICATE = "message.pitchperfect.song_paste_fail_duplicate"
	const val SONG_PASTE_FAIL_TO_PARSE = "message.pitchperfect.song_paste_fail_to_parse"
	const val SONG_RAW_FAIL_TO_PARSE = "message.pitchperfect.song_raw_fail_to_parse"
	const val SONG_REMOVED = "message.pitchperfect.song_removed"
	const val SHEET_MUSIC_FAIL_DUPLICATE = "message.pitchperfect.sheet_music_fail_duplicate"

	fun add(provider: ModLanguageProvider) {
		provider.add(INSTRUMENT_BROKEN, "Your instrument has broken!")
		provider.add(SONGS_LIST, "Songs:")
		provider.add(SONG_COPIED, "Song copied to clipboard!")
		provider.add(SONG_PASTED, "Song pasted from clipboard!")
		provider.add(SONG_PASTE_ADDED, "Song added: %s")
		provider.add(CLICK_COPY_SONG_UUID, "Click to copy UUID:\n%s")
		provider.add(CLICK_COPY_RAW_SONG, "Click to copy raw song data:\n%s")
		provider.add(CLICK_PLAY_SONG, "Click to play song.")
		provider.add(SONG_PASTE_FAIL_DUPLICATE, "Failed to add song, as an identical song already exists!\n")
		provider.add(SONG_PASTE_FAIL_TO_PARSE, "Failed to parse song from clipboard:\n%s")
		provider.add(SONG_RAW_FAIL_TO_PARSE, "Failed to parse song:\n%s")
		provider.add(SONG_REMOVED, "Song removed: %s by %s")
		provider.add(SHEET_MUSIC_FAIL_DUPLICATE, "Failed to save song, as it is a duplicate of an existing song:%s")
	}
}