package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInProgress
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
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

    private var inProgressSong: SongInProgress? = null

    fun clickCell(
        player: Player,
        delay: Int,
        pitch: Int,
        leftClick: Boolean,
        instrument: Holder<SoundEvent>
    ) {
        if (inProgressSong == null) {
            inProgressSong = SongInProgress(
                "Untitled",
                player.uuid,
                player.gameProfile.name
            )
        }

        if (leftClick) {
            inProgressSong?.incrementInstrument(delay, pitch, instrument)
        } else {
            inProgressSong?.decrementInstrument(delay, pitch, instrument)
        }
    }

}