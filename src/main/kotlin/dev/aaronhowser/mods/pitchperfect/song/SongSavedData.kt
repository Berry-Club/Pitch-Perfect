package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
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

        fun get(entity: Entity): SongSavedData {
            val level = entity.server?.overworld() ?: throw IllegalArgumentException("Entity must be serverside")

            return get(level)
        }
    }

    fun addSongInfo(song: Song, title: String, author: Player): SongInfo {
        val songInfo = SongInfo(
            title,
            author,
            song
        )

        addSongInfo(songInfo)
        return songInfo
    }

    fun addSongInfo(songInfo: SongInfo) {
        val existingSong = songs.getOrDefault(songInfo.song.uuid, null)

        if (existingSong == null) {
            songs[songInfo.song.uuid] = songInfo
            setDirty()
        } else {
            PitchPerfect.LOGGER.error("Attempted to add a song that already exists! $songInfo")
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
        return songs.values.filter { it.authorUuid == author }
    }

    fun getSongInfosGroupedByAuthor(): List<SongInfo> {
        val compareBy: Comparator<SongInfo> = compareBy(SongInfo::authorName, SongInfo::title)
        return songs.values.sortedWith(compareBy)
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