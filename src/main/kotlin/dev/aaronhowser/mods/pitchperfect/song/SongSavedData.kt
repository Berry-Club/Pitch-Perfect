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

    private val songs: MutableMap<UUID, SongInfo> = mutableMapOf()

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

                songData.songs[songInfo.uuid] = songInfo
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

    fun addSong(song: Song, title: String, author: Player): SongInfo {
        val songInfo = SongInfo(
            title,
            author.uuid,
            song
        )

        addSong(songInfo)
        return songInfo
    }

    fun addSong(songInfo: SongInfo) {
        songs[songInfo.uuid] = songInfo
        setDirty()
    }

    fun removeSong(uuid: UUID) {
        val song = songs.getOrDefault(uuid, null) ?: return
        removeSong(song)
    }

    fun removeSong(song: SongInfo) {
        songs.remove(song.uuid)
        setDirty()
    }

    fun getSong(uuid: UUID): SongInfo? {
        return songs.getOrDefault(uuid, null)
    }

    fun getSongs(): List<SongInfo> {
        return songs.values.toList()
    }

    fun getSongsBy(author: Player): List<SongInfo> {
        return getSongsBy(author.uuid)
    }

    fun getSongsBy(author: UUID): List<SongInfo> {
        return songs.values.filter { it.author == author }
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val songListTag = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())

        for (songInfo in songs.values) {
            songListTag.add(songInfo.toCompoundTag())
        }

        pTag.put(SONGS_TAG, songListTag)

        return pTag
    }
}