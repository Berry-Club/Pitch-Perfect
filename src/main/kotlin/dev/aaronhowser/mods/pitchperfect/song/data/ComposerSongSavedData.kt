package dev.aaronhowser.mods.pitchperfect.song.data

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ComposerSongSavedData : SavedData() {

	private val composerSongs: MutableMap<UUID, ComposerSong> = mutableMapOf()

	fun getOrCreateSong(uuid: UUID): ComposerSong {
		return composerSongs.computeIfAbsent(uuid) { ComposerSong(uuid, Song()) }
	}

	override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
		val songListTag = pTag.getList(COMPOSER_SONGS_TAG, Tag.TAG_COMPOUND.toInt())

		for (composerSong in composerSongs.values) {
			val tag = ComposerSong.CODEC
				.encodeStart(NbtOps.INSTANCE, composerSong)
				.result()
				.getOrNull()
				?: continue

			songListTag.add(tag)
		}

		pTag.put(COMPOSER_SONGS_TAG, songListTag)

		return pTag
	}

	companion object {
		private const val COMPOSER_SONGS_TAG = "composer_songs"

		private fun load(pTag: CompoundTag, provider: HolderLookup.Provider): ComposerSongSavedData {
			val songData = ComposerSongSavedData()
			songData.composerSongs.clear()

			val composerSongListTag = pTag.getList(COMPOSER_SONGS_TAG, Tag.TAG_COMPOUND.toInt())

			for (i in composerSongListTag.indices) {
				val composerSongTag = composerSongListTag[i] as CompoundTag
				val composerSong = ComposerSong.CODEC
					.parse(NbtOps.INSTANCE, composerSongTag)
					.result()
					.getOrNull()

				if (composerSong == null) {
					PitchPerfect.LOGGER.error("Failed to load song from tag: $composerSongTag")
					continue
				}

				songData.composerSongs[composerSong.uuid] = composerSong
			}

			return songData
		}

		fun get(level: ServerLevel): ComposerSongSavedData {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			return level.dataStorage.computeIfAbsent(
				Factory(::ComposerSongSavedData, ::load),
				"pp_composer_songs"
			)
		}
	}

}