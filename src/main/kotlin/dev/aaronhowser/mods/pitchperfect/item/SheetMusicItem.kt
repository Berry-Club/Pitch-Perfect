package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.event.OtherEvents
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent.Companion.isTrue
import dev.aaronhowser.mods.pitchperfect.item.component.SongItemComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.ClientUtil
import dev.aaronhowser.mods.pitchperfect.util.ModClientScheduler
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import kotlin.random.Random

class SheetMusicItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        fun playSounds(musicStack: ItemStack, player: Player) {
            val musicInstructions = musicStack.get(SongItemComponent.component) ?: return
            val beats = musicInstructions.beats

            var currentDelay = 0

            for (beat in beats) {
                currentDelay += beat.delayBefore
                for ((instrument, pitches) in beat.sounds) {
                    for (pitch in pitches) {
                        ModClientScheduler.scheduleTaskInTicks(currentDelay) {
                            ClientUtil.playNote(
                                instrument.soundEvent.value(),
                                pitch,
                                player.x,
                                player.eyeY ,
                                player.z,
                                false
                            )
                        }
                    }
                }
            }
        }

        fun createRandomMusicSheet(): ItemStack {
            val beats = mutableListOf<SongItemComponent.SoundsWithDelayAfter>()

            val validInstruments = NoteBlockInstrument.entries.filter { it.isTunable }

            repeat(16) {
                val instrument = validInstruments.random()

                val pitches = mutableListOf<Float>()
                repeat(Random.nextInt(1, 4)) {
                    pitches += Random.nextFloat()
                }

                beats += SongItemComponent.SoundsWithDelayAfter(
                    sounds = mapOf(instrument to pitches),
                    delayBefore = Random.nextInt(1, 4)
                )
            }

            val musicInstructions = SongItemComponent(beats)

            val musicStack = ModItems.MUSIC_SHEET.toStack()
            musicStack.set(SongItemComponent.component, musicInstructions)

            return musicStack
        }

        fun toggleRecording(stack: ItemStack, player: Player) {
            if (isRecording(stack)) {
                stopRecording(stack, player)
            } else {
                stack.set(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(true))
            }
        }

        private fun stopRecording(itemStack: ItemStack, player: Player) {
            itemStack.remove(BooleanItemComponent.isRecordingComponent)

            val songBuilder = OtherEvents.builders[player] ?: return
            OtherEvents.builders.remove(player)
            val songComponent = songBuilder.build()

            itemStack.set(SongItemComponent.component, songComponent)
        }

        fun isRecording(stack: ItemStack): Boolean {
            return stack.get(BooleanItemComponent.isRecordingComponent).isTrue
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = pPlayer.getItemInHand(pUsedHand)

        if (pLevel.isClientSide) return InteractionResultHolder.pass(stack)

        if (pPlayer.isShiftKeyDown) {
            toggleRecording(stack, pPlayer)
            return InteractionResultHolder.success(stack)
        }

        playSounds(stack, pPlayer)

        return InteractionResultHolder.success(stack)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

}
