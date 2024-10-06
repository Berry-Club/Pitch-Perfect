package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.item.component.ComposerSongComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.song.parts.Author
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
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
        const val COMPOSER_SONG_TAG = "composer_song"
    }

    var composerSong: ComposerSong? = null
        private set(value) {
            field = value
            setChanged()
        }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.loadAdditional(pTag, pRegistries)

        val composerSongTag = pTag.getCompound(COMPOSER_SONG_TAG)
        composerSong = ComposerSong.fromCompoundTag(composerSongTag)

        // this is so fucked up lmao
        if (pTag.contains("components")) {
            pTag.remove("components")
        }
    }

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.saveAdditional(pTag, pRegistries)

        val currentComposerSong = composerSong
        if (currentComposerSong != null) {
            pTag.put(COMPOSER_SONG_TAG, currentComposerSong.toTag())
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

            addAuthor(player)
        }

        setChanged()
    }

    fun setSong(song: Song, player: Player) {
        composerSong = ComposerSong(song.uuid, song, mutableListOf(Author(player)))
    }

    fun setSong(song: ComposerSong) {
        composerSong = song
    }

    override fun collectImplicitComponents(pComponents: DataComponentMap.Builder) {
        super.collectImplicitComponents(pComponents)

        val currentSong = composerSong ?: return
        pComponents.set(ModDataComponents.COMPOSER_SONG_COMPONENT, ComposerSongComponent(currentSong))
    }

}