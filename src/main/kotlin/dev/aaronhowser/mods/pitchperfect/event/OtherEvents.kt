package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.item.MusicSheetItem
import dev.aaronhowser.mods.pitchperfect.item.component.SongBuilderComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
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

        val currentWorldTick = (event.level as? Level)?.gameTime ?: throw IllegalStateException()

        for (player in nearbyRecordingPlayers) {
            val musicStack =
                player.inventory.items.first { stack -> MusicSheetItem.isRecording(stack) }

            val songBuilderComponent = musicStack.get(SongBuilderComponent.component) ?: throw IllegalStateException()

            val map: Map<Long, Map<NoteBlockInstrument, List<Float>>> = songBuilderComponent.map

            val mapForTick: Map<NoteBlockInstrument, List<Float>> = map.getOrDefault(currentWorldTick, mutableMapOf())
            val pitchesForInstrument = mapForTick.getOrDefault(instrument, mutableListOf())

            val newPitches = pitchesForInstrument + pitch
            val newMapForTick: Map<NoteBlockInstrument, List<Float>> = mapOf(instrument to newPitches)

            val newMap = map.toMutableMap()
            newMap[currentWorldTick] = newMapForTick

            val newComponent = SongBuilderComponent(currentWorldTick, newMap)

            musicStack.set(SongBuilderComponent.component, newComponent)
        }
    }

}