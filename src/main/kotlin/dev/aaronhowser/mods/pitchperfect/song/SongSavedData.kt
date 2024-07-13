package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.world.level.saveddata.SavedData

class SongSavedData : SavedData() {

    private val songs: MutableList<Pair<String, Song>> = mutableListOf()

    companion object {
        fun create() = SongSavedData()

        private const val SONGS_TAG = "songs"

        fun load(pTag: CompoundTag, provider: HolderLookup.Provider): SongSavedData {
            val songData = SongSavedData()

            val songList = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())



            return songData
        }
    }

    fun addSong(name: String, song: Song) {
        songs.add(name to song)
        setDirty()
    }

    fun removeSong(song: Song) {
        val success = songs.removeIf { it.second == song }
        if (success) setDirty()
    }

    fun getSongs(name: String): List<Song> {
        return songs.filter { it.first == name }.map { it.second }
    }

    fun getAllSongs(): List<Pair<String, Song>> {
        return songs
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        TODO("Not yet implemented")
    }
}