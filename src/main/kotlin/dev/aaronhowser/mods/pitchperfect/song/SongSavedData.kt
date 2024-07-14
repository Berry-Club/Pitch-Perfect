package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.level.saveddata.SavedData

class SongSavedData : SavedData() {

    private val songs: MutableList<Song> = mutableListOf()

    companion object {
        fun create() = SongSavedData()

        private const val SONGS_TAG = "songs"

        fun load(pTag: CompoundTag, provider: HolderLookup.Provider): SongSavedData {
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
    }

    fun addSong(song: Song) {
        songs.add(song)
        setDirty()
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()

        val songListTag = tag.getList(SONGS_TAG, Tag.TAG_STRING.toInt())

        for (song in songs) {
            val songString = song.toString()
            songListTag.add(StringTag.valueOf(songString))
        }

        tag.put(SONGS_TAG, songListTag)

        return tag
    }
}