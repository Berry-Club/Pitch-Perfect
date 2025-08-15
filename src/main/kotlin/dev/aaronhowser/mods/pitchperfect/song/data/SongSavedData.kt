package dev.aaronhowser.mods.pitchperfect.song.data

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.song.parts.SavedSong
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import java.util.*
import kotlin.jvm.optionals.getOrNull

class SongSavedData : SavedData() {

	private val savedSongs: MutableMap<UUID, SavedSong> = mutableMapOf()

	data class AddSongResult(
		val savedSong: SavedSong,
		val success: Boolean
	)

	fun addSongInfo(song: Song, title: String, author: Player): AddSongResult {
		val savedSong = SavedSong(
			title,
			author,
			song
		)

		return addSongInfo(savedSong)
	}

	fun addSongInfo(savedSong: SavedSong): AddSongResult {
		val existingSong = savedSongs[savedSong.song.uuid]

		if (existingSong == null) {
			savedSongs[savedSong.song.uuid] = savedSong
			setDirty()
			return AddSongResult(savedSong, true)
		} else {
			PitchPerfect.LOGGER.error("Attempted to add a song that already exists! $savedSong")
			return AddSongResult(existingSong, false)
		}
	}

	fun removedSavedSong(uuid: UUID) {
		val song = savedSongs.getOrDefault(uuid, null) ?: return
		removedSavedSong(song)
	}

	fun removedSavedSong(song: SavedSong) {
		savedSongs.remove(song.song.uuid)
		setDirty()
	}

	fun getSavedSong(uuid: UUID): SavedSong? {
		return savedSongs.getOrDefault(uuid, null)
	}

	fun getSavedSongsGroupedByAuthor(): List<SavedSong> {
		val compareBy: Comparator<SavedSong> = compareBy(SavedSong::title)
		return savedSongs.values.sortedWith(compareBy)
	}

	override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
		val songListTag = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())

		for (songInfo in savedSongs.values) {
			val tag = SavedSong.CODEC
				.encodeStart(NbtOps.INSTANCE, songInfo)
				.result()
				.getOrNull()
				?: continue

			songListTag.add(tag)
		}

		pTag.put(SONGS_TAG, songListTag)

		return pTag
	}

	companion object {
		private const val SONGS_TAG = "songs"

		private fun load(pTag: CompoundTag, provider: HolderLookup.Provider): SongSavedData {
			val songData = SongSavedData()
			songData.savedSongs.clear()

			val savedSongsListTag = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())

			for (i in savedSongsListTag.indices) {
				val savedSongTag = savedSongsListTag[i]
				val savedSong = SavedSong.CODEC
					.parse(NbtOps.INSTANCE, savedSongTag)
					.result()
					.getOrNull()

				if (savedSong == null) {
					PitchPerfect.LOGGER.error("Failed to load song from tag: $savedSongTag")
					continue
				}

				songData.savedSongs[savedSong.song.uuid] = savedSong
			}

			return songData
		}

		fun get(level: ServerLevel): SongSavedData {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			return level.dataStorage.computeIfAbsent(
				Factory(::SongSavedData, Companion::load),
				"pp_saved_songs"
			)
		}
	}

}