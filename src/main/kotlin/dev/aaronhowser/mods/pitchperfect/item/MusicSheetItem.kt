package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.item.component.SongBuilderComponent
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

class MusicSheetItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        fun playSounds(musicStack: ItemStack, player: Player) {
            val musicInstructions = musicStack.get(SongItemComponent.component) ?: return
            val beats = musicInstructions.beats

            var currentDelay = 0

            for (beat in beats) {
                for ((instrument, pitches) in beat.sounds) {
                    for (pitch in pitches) {
                        ModClientScheduler.scheduleTaskInTicks(currentDelay) {
                            ClientUtil.playNote(
                                instrument.soundEvent.value(),
                                pitch,
                                player.x,
                                player.y + 2,
                                player.z,
                                false
                            )
                        }
                    }
                }
                currentDelay += beat.delayAfter
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
                    delayAfter = Random.nextInt(1, 4)
                )
            }

            val musicInstructions = SongItemComponent(beats)

            val musicStack = ModItems.MUSIC_SHEET.toStack()
            musicStack.set(SongItemComponent.component, musicInstructions)

            return musicStack
        }

        fun toggleRecording(stack: ItemStack, level: Level) {
            if (isRecording(stack)) {
                stack.remove(SongBuilderComponent.component)
            } else {
                stack.set(SongBuilderComponent.component, SongBuilderComponent(level.gameTime, emptyMap()))
            }
        }

        fun isRecording(stack: ItemStack): Boolean {
            return stack.has(SongBuilderComponent.component)
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        if (pLevel.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand))

        if (pPlayer.isCrouching) {
            toggleRecording(pPlayer.getItemInHand(pUsedHand), pLevel)
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand))
        }

        val newMusicStack = createRandomMusicSheet()
        pPlayer.setItemInHand(pUsedHand, newMusicStack)
        playSounds(newMusicStack, pPlayer)

        return InteractionResultHolder.success(newMusicStack)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

}
