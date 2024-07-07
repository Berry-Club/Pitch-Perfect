package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.item.MusicSheetItem
import dev.aaronhowser.mods.pitchperfect.item.component.LongItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.util.Mth
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
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
    fun onNoteBlock(event: NoteBlockEvent.Play) {
        if (event.level.isClientSide) return

        val blockPos = event.pos

        val nearbyRecordingPlayers = event.level.players().filter { player ->
            player.inventory.contains { stack -> MusicSheetItem.isRecording(stack) } &&
                    player.blockPosition().distSqr(blockPos) < Mth.square(64)
        }

        val pitch = event.vanillaNoteId.toFloat().map(0f, 24f, 0f, 1f)
        val instrument = event.instrument

        val currentWorldTick = event.level.server?.tickCount ?: throw IllegalStateException()

        val beat = SongItemComponent.SoundsWithDelayAfter(
            mapOf(instrument to listOf(pitch)),
            1
        )

        //TODO: Make a InProgressSongComponent
        for (player in nearbyRecordingPlayers) {

            val musicStack =
                player.inventory.items.first { stack -> MusicSheetItem.isRecording(stack) }

            val sheetStartTime =
                musicStack.get(LongItemComponent.startedRecordingAt)?.long ?: throw IllegalStateException()

            val ticksSinceStarting = currentWorldTick - sheetStartTime

            val currentSong =
                musicStack.get(SongItemComponent.component) ?: throw IllegalStateException()

            val currentBeats = currentSong.beats

            val newBeats = currentBeats + beat

            val newSong = SongItemComponent(newBeats)

            musicStack.set(SongItemComponent.component, newSong)
        }

    }

}