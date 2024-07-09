package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.block.ConductorBlock
import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class ConductorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.CONDUCTOR.get(), pPos, pBlockState) {

    private val itemHandler: ItemStackHandler = object : ItemStackHandler(1) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return stack.item == ModItems.MUSIC_SHEET.get()
        }
    }

    fun getItemHandler(side: Direction?): IItemHandler {
        return itemHandler
    }

    override fun setChanged() {
        super.setChanged()

        blockState.setValue(
            ConductorBlock.FILLED,
            !itemHandler.getStackInSlot(0).isEmpty
        )

        level?.sendBlockUpdated(blockPos, blockState, blockState, 1 or 2)   // 1 = update adjacent, 2 = send to client
    }

    fun playerClick(player: Player) {
        val blockItem = itemHandler.getStackInSlot(0)
        val handItem = player.getItemInHand(player.usedItemHand)

        if (blockItem.isEmpty) {
            if (!itemHandler.isItemValid(0, handItem)) return

            itemHandler.setStackInSlot(0, handItem.copy())
            player.setItemInHand(player.usedItemHand, ItemStack.EMPTY)
        } else {

            if (handItem.isEmpty) {
                player.setItemInHand(player.usedItemHand, blockItem.copy())
            } else {
                if (!player.inventory.add(blockItem.copy())) {
                    player.drop(blockItem.copy(), false)
                }
            }

            itemHandler.setStackInSlot(0, ItemStack.EMPTY)
        }
    }

    fun dropDrops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

}