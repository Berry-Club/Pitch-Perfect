package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class SongSavedData : SavedData() {

    private val songs: MutableList<SongInfo> = mutableListOf()

    companion object {
        private fun create() = SongSavedData()

        private const val SONGS_TAG = "songs"

        private fun load(pTag: CompoundTag, provider: HolderLookup.Provider): SongSavedData {
            val songData = SongSavedData()
            songData.songs.clear()

            val songInfoListTag = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())

            for (i in songInfoListTag.indices) {
                val songInfoTag = songInfoListTag[i] as CompoundTag
                val songInfo = SongInfo.fromCompoundTag(songInfoTag)

                songData.songs.add(songInfo)
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

    fun addSong(song: Song, title: String, author: Player) {
        val songInfo = SongInfo(
            title,
            author.uuid,
            song
        )

        addSong(songInfo)
    }

    fun addSong(songInfo: SongInfo) {
        songs.add(songInfo)
        setDirty()
    }

    fun removeSong(uuid: UUID) {
        val song = songs.firstOrNull { it.uuid == uuid } ?: return
        removeSong(song)
    }

    fun removeSong(song: SongInfo) {
        songs.remove(song)
        setDirty()
    }

    fun getSong(uuid: UUID): SongInfo? {
        return songs.firstOrNull { it.uuid == uuid }
    }

    fun getSongs(): List<SongInfo> {
        return songs.toList()
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val songListTag = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())

        for (songInfo in songs) {
            songListTag.add(songInfo.toCompoundTag())
        }

        pTag.put(SONGS_TAG, songListTag)

        return pTag
    }
}