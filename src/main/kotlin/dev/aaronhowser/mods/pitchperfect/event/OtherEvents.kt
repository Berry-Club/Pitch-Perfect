package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.command.ModCommands
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.item.SheetMusicItem
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.song.SongRecorder
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.NoteBlock
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
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

    val songRecorders = mutableMapOf<Player, SongRecorder>()

    @SubscribeEvent
    fun onNoteBlock(event: NoteBlockEvent.Play) {
        if (event.level.isClientSide) return

        val blockPos = event.pos

        val pitch = NoteBlock.getPitchFromNote(event.vanillaNoteId)

        val soundEvent = event.instrument.soundEvent

        val currentWorldTick = (event.level as? Level)?.gameTime ?: throw IllegalStateException()

        val nearbyRecordingPlayers = event.level.players().filter { player ->
            player.inventory.contains { stack -> SheetMusicItem.isRecording(stack) } &&
                    player.blockPosition().distSqr(blockPos) < Mth.square(ServerConfig.RECORDING_RANGE.get())
        }

        for (player in nearbyRecordingPlayers) {
            val songRecorder = songRecorders.getOrPut(player) { SongRecorder(currentWorldTick) }

            songRecorder.addNote(
                currentWorldTick,
                soundEvent,
                pitch
            )
        }

    }

    @SubscribeEvent
    fun onActivateEntity(event: PlayerInteractEvent.EntityInteractSpecific) {
        if (event.level.isClientSide) return

        val armorStand = event.target as? ArmorStand ?: return
        if (!ServerConfig.CAN_GIVE_ARMOR_STANDS_INSTRUMENTS.get()) return

        val player = event.entity

        val armorStandItem = armorStand.mainHandItem
        if (armorStandItem.isEmpty) {
            val playerItem = player.getItemInHand(event.hand)
            if (!playerItem.has(ModDataComponents.SOUND_EVENT_COMPONENT)) return

            armorStand.disabledSlots = -1
            armorStand.isShowArms = true

            armorStand.setItemInHand(InteractionHand.MAIN_HAND, playerItem.copy())
            playerItem.shrink(1)
        } else {
            if (!armorStandItem.has(ModDataComponents.SOUND_EVENT_COMPONENT)) return

            player.addItem(armorStandItem.copy())
            armorStandItem.shrink(1)
        }

        event.level.playSound(null, armorStand.blockPosition(), SoundEvents.DISPENSER_DISPENSE, SoundSource.PLAYERS)
    }

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        ModCommands.register(event.dispatcher)
    }

}