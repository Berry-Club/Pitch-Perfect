package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.song.parts.Author
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ComposerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.COMPOSER.get(), pPos, pBlockState) {

    companion object {
        const val COMPOSER_SONG_TAG = "composer_song"
        const val AUTHORS_TAG = "authors"
        const val AUTHOR_NAME_TAG = "name"
        const val AUTHOR_UUID_TAG = "uuid"
    }

    var composerSong: ComposerSong? = null
        private set
    var authors: List<Author> = emptyList()
        private set

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.loadAdditional(pTag, pRegistries)

        val composerSongTag = pTag.getCompound(COMPOSER_SONG_TAG)
        composerSong = ComposerSong.fromCompoundTag(composerSongTag)

        val authorsTag = pTag.getList(AUTHORS_TAG, ListTag.TAG_COMPOUND.toInt())
        for (authorTag in authorsTag) {
            if (authorTag !is CompoundTag) continue

            val name = authorTag.getString(AUTHOR_NAME_TAG)
            val uuid = authorTag.getUuidOrNull(AUTHOR_UUID_TAG) ?: continue

            authors = authors + Author(uuid, name)
        }
    }

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.saveAdditional(pTag, pRegistries)

        val timeline = composerSong
        if (timeline != null) {
            pTag.put(COMPOSER_SONG_TAG, timeline.toTag())
        }

        if (authors.isNotEmpty()) {
            val authorsTag = pTag.getList(AUTHORS_TAG, ListTag.TAG_COMPOUND.toInt())

            for (author in authors) {
                val authorTag = CompoundTag()
                authorTag.putString(AUTHOR_NAME_TAG, author.name)
                authorTag.putString(AUTHOR_UUID_TAG, author.uuid.toString())
                authorsTag.add(authorTag)
            }

            pTag.put(AUTHORS_TAG, authorsTag)
        }
    }

    override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(pRegistries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun setChanged() {
        super.setChanged()

        level?.sendBlockUpdated(blockPos, blockState, blockState, 1 or 2 or 8)
    }

    fun clickCell(
        player: Player,
        delay: Int,
        pitch: Int,
        leftClick: Boolean,
        instrument: String
    ) {
        if (composerSong == null) {
            composerSong = ComposerSong()
        }

        if (authors.none { it.uuid == player.uuid }) {
            authors = authors + Author(player.uuid, player.gameProfile.name)
        }

        val note = Note.getFromPitch(pitch)

        val soundHolder = Song.getSoundHolder(instrument)

        composerSong?.apply {
            if (leftClick) {
                addBeat(delay, note, soundHolder)
            } else {
                removeBeat(delay, note, soundHolder)
            }

            setChanged()
        }
    }

    fun setSong(song: Song) {
        composerSong = ComposerSong(song)
        setChanged()
    }

}