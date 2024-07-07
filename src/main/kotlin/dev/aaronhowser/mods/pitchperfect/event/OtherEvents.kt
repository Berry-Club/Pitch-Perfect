package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.item.MusicSheetItem
import dev.aaronhowser.mods.pitchperfect.song.SongBuilder
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
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

    val builders = mutableMapOf<Player, SongBuilder>()

    @SubscribeEvent
    fun onNoteBlock(event: NoteBlockEvent.Play) {
        if (event.level.isClientSide) return

        val blockPos = event.pos

        val pitch = event.vanillaNoteId.toFloat().map(0f, 24f, 0f, 1f)
        val instrument = event.instrument

        val currentWorldTick = (event.level as? Level)?.gameTime ?: throw IllegalStateException()

        val nearbyRecordingPlayers = event.level.players().filter { player ->
            player.inventory.contains { stack -> MusicSheetItem.isRecording(stack) } &&
                    player.blockPosition().distSqr(blockPos) < Mth.square(64)
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

}