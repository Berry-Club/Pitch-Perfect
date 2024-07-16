package dev.aaronhowser.mods.pitchperfect.menu

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import dev.aaronhowser.mods.pitchperfect.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.SlotItemHandler

class ComposerMenu(
    id: Int,
    inventory: Inventory,
    private val blockEntity: ComposerBlockEntity,
    private val containerData: ContainerData
) : AbstractContainerMenu(ModMenuTypes.COMPOSER.get(), id) {

    constructor(id: Int, inventory: Inventory, extraData: RegistryFriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level().getBlockEntity(extraData.readBlockPos()) as ComposerBlockEntity,
                SimpleContainerData(1)  //TODO?
            )

    init {
        checkContainerSize(inventory, 1) //TODO?

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        val itemHandler = this.blockEntity.getItemHandler(null)

        this.addSlot(
            SlotItemHandler(itemHandler, 0, 63, 36)
        )

        addDataSlots(containerData)
    }

    val level: Level = inventory.player.level()

    companion object {
        private const val topLeftInventoryX = 8
        private const val topLeftInventoryY = 84
    }

    // Adds the 27 slots of the player inventory
    private fun addPlayerInventory(playerInventory: Inventory) {
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(
                    Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        topLeftInventoryX + column * 18,
                        topLeftInventoryY + row * 18
                    )
                )
            }
        }
    }

    // Adds the 9 slots of the player hotbar
    private fun addPlayerHotbar(playerInventory: Inventory) {

        val hotbarGapHeight = 4

        for (column in 0 until 9) {
            addSlot(
                Slot(
                    playerInventory,
                    column,
                    column * 18,
                    topLeftInventoryY + hotbarGapHeight + 3 * 18
                )
            )
        }
    }

    override fun quickMoveStack(pPlayer: Player, pIndex: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.COMPOSER.get()
        )
    }
}