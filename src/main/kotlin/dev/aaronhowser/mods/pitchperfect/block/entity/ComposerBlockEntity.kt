package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
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

    fun dropDrops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

    fun playerClick(player: Player) {
        player.sendSystemMessage(Component.literal("Hi!"))
    }

}