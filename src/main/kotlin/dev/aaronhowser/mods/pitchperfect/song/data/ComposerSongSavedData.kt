package dev.aaronhowser.mods.pitchperfect.song.data

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class ComposerSongSavedData : SavedData() {

    private val composerSongs: MutableMap<UUID, ComposerSong> = mutableMapOf()

    companion object {
        private const val COMPOSER_SONGS_TAG = "composer_songs"

        private fun load(pTag: CompoundTag, provider: HolderLookup.Provider): ComposerSongSavedData {
            val songData = ComposerSongSavedData()
            songData.composerSongs.clear()

            val composerSongListTag = pTag.getList(COMPOSER_SONGS_TAG, Tag.TAG_COMPOUND.toInt())

            for (i in composerSongListTag.indices) {
                val composerSongTag = composerSongListTag[i] as CompoundTag
                val composerSong = ComposerSong.fromCompoundTag(composerSongTag)

                if (composerSong == null) {
                    PitchPerfect.LOGGER.error("Failed to load song from tag: $composerSongTag")
                    continue
                }

                songData.composerSongs[composerSong.uuid] = composerSong
            }

            return songData
        }

        fun get(level: ServerLevel): ComposerSongSavedData {
            require(level == level.server.overworld()) { "SongSavedData can only be accessed on the overworld" }

            return level.dataStorage.computeIfAbsent(
                Factory(::ComposerSongSavedData, Companion::load),
                "pitchperfect"
            )
        }

        val ServerLevel.composerSongSavedData: ComposerSongSavedData
            get() = get(this.server.overworld())
        val MinecraftServer.composerSongSavedData: ComposerSongSavedData
            get() = get(this.overworld())

    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val songListTag = pTag.getList(COMPOSER_SONGS_TAG, Tag.TAG_COMPOUND.toInt())

        for (songInfo in composerSongs.values) {
            songListTag.add(songInfo.toTag())
        }

        pTag.put(COMPOSER_SONGS_TAG, songListTag)

        return pTag
    }

}