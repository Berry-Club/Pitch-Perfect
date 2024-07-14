package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData

class SongSavedData : SavedData() {

    private val songs: MutableList<Song> = mutableListOf()

    companion object {
        private fun create() = SongSavedData()

        private const val SONGS_TAG = "songs"

        private fun load(pTag: CompoundTag, provider: HolderLookup.Provider): SongSavedData {
            val songData = SongSavedData()
            songData.songs.clear()

            val songList = pTag.getList(SONGS_TAG, Tag.TAG_STRING.toInt())

            for (i in songList.indices) {
                val string = songList.getString(i)
                val song = Song.parse(string)
                songData.addSong(song)
            }

            return songData
        }

        fun get(level: ServerLevel): SongSavedData {
            require(level == level.server.overworld()) { "SongSavedData can only be accessed on the overworld" }

            return level.dataStorage.computeIfAbsent(
                Factory(::create, ::load),
                "pitchperfect"
            )
        }
    }

    fun addSong(song: Song) {
        songs.add(song)
        setDirty()
    }

    fun removeSong(song: Song) {
        songs.remove(song)
        setDirty()
    }

    fun getSongs(): List<Song> {
        return songs.toList()
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val songListTag = pTag.getList(SONGS_TAG, Tag.TAG_STRING.toInt())

        for (song in songs) {
            val songString = song.toString()
            songListTag.add(StringTag.valueOf(songString))
        }

        pTag.put(SONGS_TAG, songListTag)

        return pTag
    }
}