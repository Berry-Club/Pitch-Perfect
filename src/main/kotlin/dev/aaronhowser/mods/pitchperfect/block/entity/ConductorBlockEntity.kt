package dev.aaronhowser.mods.pitchperfect.block.entity

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.ConductorBlock
import dev.aaronhowser.mods.pitchperfect.item.component.SoundEventComponent
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.song.SongSerializer
import dev.aaronhowser.mods.pitchperfect.util.ModServerScheduler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.component1
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.component2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.component3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

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

        val newBottomState = blockState.setValue(
            ConductorBlock.FILLED,
            !itemHandler.getStackInSlot(0).isEmpty
        )

        level?.setBlock(blockPos, newBottomState, 1 or 2) // 1 = update adjacent, 2 = send to client
        level?.sendBlockUpdated(blockPos, blockState, blockState, 1 or 2)

        val oldTopState = level?.getBlockState(blockPos.above()) ?: return
        if (oldTopState.block != ModBlocks.CONDUCTOR.get()) return

        val newTopState = oldTopState.setValue(
            ConductorBlock.FILLED,
            !itemHandler.getStackInSlot(0).isEmpty
        )

        level?.setBlock(blockPos.above(), newTopState, 1 or 2)
        level?.sendBlockUpdated(blockPos.above(), oldTopState, newTopState, 1 or 2)

        song = null
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

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries))
        super.saveAdditional(pTag, pRegistries)
    }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"))
        super.loadAdditional(pTag, pRegistries)
    }

    private var song: SongSerializer.Song? = null

    fun redstonePulse() {
        val blockItem = itemHandler.getStackInSlot(0)
        if (blockItem.item != ModItems.MUSIC_SHEET.get()) return

        val song = SongSerializer.Song.fromFile(FMLPaths.CONFIGDIR.get().resolve("song.json"))
        println(song)
        this.song = song

        startPlaying()
    }

    private val nearbyArmorStands: MutableMap<SoundEvent, List<ArmorStand>> = mutableMapOf()
    private fun findNearbyArmorStands() {
        val level = level as? ServerLevel ?: return
        nearbyArmorStands.clear()

        val armorStands = level.getEntitiesOfClass(
            ArmorStand::class.java,
            AABB.ofSize(blockPos.toVec3(), 16.0, 16.0, 16.0)
        )

        for (armorStand in armorStands) {
            val heldItem = armorStand.mainHandItem

            val itemSound = SoundEventComponent.getSoundEvent(heldItem) ?: continue

            val armorStandsWithInstrument = nearbyArmorStands.getOrPut(itemSound) { emptyList() }.toMutableList()
            armorStandsWithInstrument.add(armorStand)
            nearbyArmorStands[itemSound] = armorStandsWithInstrument
        }
    }

    private fun startPlaying() {
        findNearbyArmorStands()
        val cachedSong = song ?: return

        for ((instrument, beats) in cachedSong.instruments) {
            val soundEvent = instrument.getSoundEvent()

            val armorStands = nearbyArmorStands[soundEvent]
            if (armorStands == null) {
                PitchPerfect.LOGGER.error("No armor stands found for $soundEvent")
                continue
            }

            for ((delay, notes) in beats) {

                for (note in notes) {
                    val pitch = note.getGoodPitch()

                    val location = armorStands.randomOrNull()?.eyePosition ?: blockPos.toVec3().add(0.5, 1.5, 0.5)

                    ModServerScheduler.scheduleTaskInTicks(delay) {

                        val (x, y, z) = location

                        ModPacketHandler.messageNearbyPlayers(
                            SpawnNotePacket(
                                soundEvent.location,
                                pitch,
                                x,
                                y,
                                z,
                                false
                            ),
                            level as ServerLevel,
                            location,
                            128.0
                        )

                    }

                }

            }

        }

    }

}