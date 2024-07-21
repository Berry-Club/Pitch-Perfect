package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.block.entity.composer.ComposerTimeline
import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class ComposerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.COMPOSER.get(), pPos, pBlockState) {

    companion object {
        const val AMOUNT_SLOTS = 1
        const val SHEET_MUSIC_SLOT = 0

        const val TIMELINE = "timeline"
    }

    private val itemHandler: ItemStackHandler = object : ItemStackHandler(AMOUNT_SLOTS) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                SHEET_MUSIC_SLOT -> stack.item == ModItems.MUSIC_SHEET.get()
                else -> false
            }
        }
    }

    fun getItemHandler(side: Direction?): IItemHandler {
        return itemHandler
    }

    fun dropDrops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

    fun getItem(): ItemStack {
        return itemHandler.getStackInSlot(SHEET_MUSIC_SLOT)
    }

    var composerTimeline: ComposerTimeline? = null

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.loadAdditional(pTag, pRegistries)

        composerTimeline = ComposerTimeline.fromCompoundTag(pTag.getCompound(TIMELINE))
        println("Loaded composer timeline: $composerTimeline with soundcounts:\n${composerTimeline?.soundCounts}")
    }

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        super.saveAdditional(pTag, pRegistries)

        val timeline = composerTimeline
        if (timeline != null) {
            pTag.put(TIMELINE, timeline.toTag())
            println("Saved composer timeline: $timeline with soundcounts:\n${timeline.soundCounts}")
        }
    }

    override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(pRegistries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    fun clickCell(
        player: Player,
        delay: Int,
        pitch: Int,
        leftClick: Boolean,
        instrument: String
    ) {

        if (composerTimeline == null) {
            composerTimeline = ComposerTimeline()
        }

        composerTimeline?.apply {
            if (leftClick) {
                addSoundAt(delay, pitch, instrument)
            } else {
                removeSoundAt(delay, pitch, instrument)
            }

            setChanged()
        }
    }

}