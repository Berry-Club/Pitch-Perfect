package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.item.SheetMusicItem
import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
import dev.aaronhowser.mods.pitchperfect.song.SongBuilder
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.NoteBlock
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.level.NoteBlockEvent

@EventBusSubscriber(
    modid = PitchPerfect.ID
)
object OtherEvents {

    @SubscribeEvent
    fun afterLivingHurt(event: LivingDamageEvent.Post) {
        AndHisMusicWasElectricEnchantment.handleElectric(event)
    }

    @SubscribeEvent
    fun onChat(event: ServerChatEvent) {
        val player = event.player
        val heldItem = player.mainHandItem
    }

    val builders = mutableMapOf<Player, SongBuilder>()

    @SubscribeEvent
    fun onNoteBlock(event: NoteBlockEvent.Play) {
        if (event.level.isClientSide) return

        val blockPos = event.pos

        val pitch = NoteBlock.getPitchFromNote(event.vanillaNoteId)

        val instrument = event.instrument

        val currentWorldTick = (event.level as? Level)?.gameTime ?: throw IllegalStateException()

        val nearbyRecordingPlayers = event.level.players().filter { player ->
            player.inventory.contains { stack -> SheetMusicItem.isRecording(stack) } &&
                    player.blockPosition().distSqr(blockPos) < Mth.square(ServerConfig.RECORDING_RANGE.get())
        }

        for (player in nearbyRecordingPlayers) {
            val songBuilder = builders.getOrPut(player) { SongBuilder(currentWorldTick) }

            songBuilder.addNote(
                currentWorldTick,
                instrument,
                pitch
            )
        }

    }

    //TODO: Maybe just Access Transform `disabledSlots` to be zero?
    @SubscribeEvent
    fun onActivateEntity(event: PlayerInteractEvent.EntityInteractSpecific) {
        val armorStand = event.target as? ArmorStand ?: return

        if (armorStand.level().isClientSide) return
        if (!ServerConfig.CAN_GIVE_ARMOR_STANDS_INSTRUMENTS.get()) return

        val player = event.entity
        val playerItem = player.getItemInHand(event.hand)

        if (!playerItem.has(InstrumentComponent.component)) return

        val armorStandItem = armorStand.mainHandItem
        if (!armorStandItem.isEmpty) return

        armorStand.isShowArms = true

        armorStand.setItemInHand(InteractionHand.MAIN_HAND, playerItem.copy())
        println(armorStand.mainHandItem)
        playerItem.shrink(1)
    }

}