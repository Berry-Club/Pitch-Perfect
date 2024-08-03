package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
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

        private const val COMPONENTS_TAG = "components"

        private val COMPOSER_SONG_TAG = ModDataComponents.COMPOSER_SONG_COMPONENT.id.toString()
    }

    var composerSong: ComposerSong? = null
        private set

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.loadAdditional(pTag, pRegistries)

        val componentsTag = pTag.getCompound(COMPONENTS_TAG)

        val songString = componentsTag.getString(COMPOSER_SONG_TAG)
        val song = Song.fromString(songString)
        if (song != null) {
            setSong(song)
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
        composerSong = ComposerSong(song, emptyList())
        setChanged()
    }

}