package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class SongSavedData : SavedData() {

    private val songs: HashMap<UUID, SongSerializer.Song> = HashMap()

    companion object {
        fun create() = SongSavedData()

        fun load(pTag: CompoundTag, provider: HolderLookup.Provider): SongSavedData {
            val songData = SongSavedData()

            val songList = pTag.getList("songs", 10)

            for (i in songList) {
                if (i !is CompoundTag) throw IllegalStateException("Expected a CompoundTag, but got $i")

            }

            return songData
        }
    }

    fun addSong(pSong: SongSerializer.Song, uuid: UUID? = null): UUID {
        val realUuid = uuid ?: UUID.randomUUID()
        songs[realUuid] = pSong
        setDirty()
        return realUuid
    }

    fun removeSong(uuid: UUID) {
        if (songs.containsKey(uuid)) {
            songs.remove(uuid)
            setDirty()
        } else {
            PitchPerfect.LOGGER.warn("Tried to remove song with UUID $uuid, but it does not exist.")
        }
    }

    fun getSong(uuid: UUID): SongSerializer.Song? {
        return songs[uuid]
    }

    fun getAllSongs(): Map<UUID, SongSerializer.Song> {
        return songs
    }

    override fun save(pTag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        TODO("Not yet implemented")
    }
}