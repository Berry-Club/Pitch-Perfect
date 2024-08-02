package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongWip
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
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
        const val SONG_WIP_TAG = "song_wip"
    }

    var songWip: SongWip? = null
        private set

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.loadAdditional(pTag, pRegistries)

        val songWipTag = pTag.getCompound(SONG_WIP_TAG)
        songWip = SongWip.fromCompoundTag(songWipTag)
    }

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.saveAdditional(pTag, pRegistries)

        val timeline = songWip
        if (timeline != null) {
            pTag.put(SONG_WIP_TAG, timeline.toTag())
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
        level?.sendBlockUpdated(blockPos, blockState, blockState, 3)
    }

    fun clickCell(
        player: Player,
        delay: Int,
        pitch: Int,
        leftClick: Boolean,
        instrument: String
    ) {
        if (songWip == null) {
            songWip = SongWip()
        }

        val note = Note.getFromPitch(pitch)

        val soundHolder = Song.getSoundHolder(instrument)

        songWip?.apply {
            if (leftClick) {
                addBeat(delay, note, soundHolder)
            } else {
                removeBeat(delay, note, soundHolder)
            }

            setChanged()
        }
    }

    fun setSong(song: Song) {
        songWip = SongWip(song)
        setChanged()
    }

}