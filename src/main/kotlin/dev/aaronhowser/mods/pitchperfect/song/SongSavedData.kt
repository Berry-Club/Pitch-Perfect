package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.MinecraftServer
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

                if (songInfo == null) {
                    PitchPerfect.LOGGER.error("Failed to load song from tag: $songInfoTag")
                    continue
                }

                songData.songs[songInfo.song.uuid] = songInfo
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

        val ServerLevel.songData: SongSavedData
            get() = get(this.server.overworld())
        val MinecraftServer.songData: SongSavedData
            get() = get(this.overworld())
    }

    data class AddSongResult(
        val songInfo: SongInfo,
        val success: Boolean
    )

    fun addSongInfo(song: Song, title: String, author: Player): AddSongResult {
        val songInfo = SongInfo(
            title,
            author,
            song
        )

        return addSongInfo(songInfo)
    }

    fun addSongInfo(songInfo: SongInfo): AddSongResult {
        val existingSong = songs[songInfo.song.uuid]

        if (existingSong == null) {
            songs[songInfo.song.uuid] = songInfo
            setDirty()
            return AddSongResult(songInfo, true)
        } else {
            PitchPerfect.LOGGER.error("Attempted to add a song that already exists! $songInfo")
            return AddSongResult(existingSong, false)
        }
    }

    fun removeSongInfo(uuid: UUID) {
        val song = songs.getOrDefault(uuid, null) ?: return
        removeSongInfo(song)
    }

    fun removeSongInfo(song: SongInfo) {
        songs.remove(song.song.uuid)
        setDirty()
    }

    fun getSongInfo(uuid: UUID): SongInfo? {
        return songs.getOrDefault(uuid, null)
    }

    fun getSongInfos(): List<SongInfo> {
        return songs.values.toList()
    }

    fun getSongInfosBy(author: Player): List<SongInfo> {
        return getSongInfosBy(author.uuid)
    }

    fun getSongInfosBy(author: UUID): List<SongInfo> {
        return songs.values.filter { songInfo -> songInfo.authors.any { it.uuid == author } }
    }

    fun getSongInfosGroupedByAuthor(): List<SongInfo> {
        val compareBy: Comparator<SongInfo> = compareBy(SongInfo::title)
        return songs.values.sortedWith(compareBy)
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val songListTag = pTag.getList(SONGS_TAG, Tag.TAG_COMPOUND.toInt())

        for (songInfo in songs.values) {
            songListTag.add(songInfo.toTag())
        }

        pTag.put(SONGS_TAG, songListTag)

        return pTag
    }
}