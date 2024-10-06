package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.getUuidOrNull
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class ComposerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.COMPOSER.get(), pPos, pBlockState) {

    companion object {
        const val COMPOSER_SONG_UUID_NBT = "composer_song_uuid"
    }

    // Defaults to a new random one, but if it's actually loading from NBT it'll be overwritten
    var composerSongUuid: UUID = UUID.randomUUID()
        private set

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.loadAdditional(pTag, pRegistries)

        val songUuid = pTag.getUuidOrNull(COMPOSER_SONG_UUID_NBT)

        if (songUuid != null) {
            composerSongUuid = songUuid
        }

        // this is so fucked up lmao
        if (pTag.contains("components")) {
            pTag.remove("components")
        }
    }

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.saveAdditional(pTag, pRegistries)

        pTag.putUUID(COMPOSER_SONG_UUID_NBT, composerSongUuid)
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

    override fun collectImplicitComponents(pComponents: DataComponentMap.Builder) {
        super.collectImplicitComponents(pComponents)

        pComponents.set(
            ModDataComponents.SONG_UUID_COMPONENT,
            UuidComponent(composerSongUuid)
        )
    }

}