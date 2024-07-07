package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent.Companion.isTrue
import dev.aaronhowser.mods.pitchperfect.item.component.MusicSheetItemComponent
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
        val blockPos = event.pos

        val nearbyRecordingPlayers = event.level.players().filter { player ->
            player.inventory.contains { stack -> stack.get(BooleanItemComponent.isRecordingComponent).isTrue } &&
                    player.blockPosition().distSqr(blockPos) < Mth.square(64)
        }

        val pitch = event.vanillaNoteId.toFloat().map(0f, 24f, 0f, 1f)
        val instrument = event.instrument

        val currentWorldTick = event.level.server?.tickCount ?: throw IllegalStateException()

        for (player in nearbyRecordingPlayers) {

            val musicStack =
                player.inventory.items.first { stack -> stack.get(BooleanItemComponent.isRecordingComponent).isTrue }

            val currentMusic =
                musicStack.get(MusicSheetItemComponent.component) ?: throw IllegalStateException()

        }

    }

}