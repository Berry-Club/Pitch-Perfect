package dev.aaronhowser.mods.pitchperfect.menu

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import dev.aaronhowser.mods.pitchperfect.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack

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

    private val level = inventory.player.level()

    companion object {
        private const val TOP_LEFT_INVENTORY_X = 8
        private const val TOP_LEFT_INVENTORY_Y = 84
    }

    init {
        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)
    }

    // Adds the 27 slots of the player inventory
    fun addPlayerInventory(playerInventory: Inventory) {
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(
                    Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        TOP_LEFT_INVENTORY_X + column * 18,
                        TOP_LEFT_INVENTORY_Y + row * 18
                    )
                )
            }
        }
    }

    // Adds the 9 slots of the player hotbar
    fun addPlayerHotbar(playerInventory: Inventory) {
        for (column in 0 until 9) {
            addSlot(
                Slot(
                    playerInventory,
                    column,
                    TOP_LEFT_INVENTORY_X + column * 18,
                    TOP_LEFT_INVENTORY_Y + 4 + 3 * 18
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