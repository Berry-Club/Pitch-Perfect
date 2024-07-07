package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.item.component.LongItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.MusicSheetItemComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.ClientUtil
import dev.aaronhowser.mods.pitchperfect.util.ModClientScheduler
import net.minecraft.core.BlockPos
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
        .component(MusicSheetItemComponent.component, MusicSheetItemComponent(emptyList()))
) {

    companion object {

        fun playSounds(musicStack: ItemStack, blockPos: BlockPos) {
            val musicInstructions = musicStack.get(MusicSheetItemComponent.component) ?: return

            val beats = musicInstructions.beats

            var currentDelay = 0

            for (beat in beats) {
                for ((instrument, pitch) in beat.sounds) {

                    ModClientScheduler.scheduleTaskInTicks(currentDelay) {
                        ClientUtil.playNote(
                            instrument.soundEvent.value(),
                            pitch,
                            blockPos.x.toDouble(),
                            blockPos.y.toDouble(),
                            blockPos.z.toDouble(),
                            false
                        )
                    }
                }

                currentDelay += beat.delayAfter
            }
        }

        fun createRandomMusicSheet(): ItemStack {
            val beats = (0 until 16).map {
                MusicSheetItemComponent.Beat(
                    listOf(
                        MusicSheetItemComponent.Beat.Doot(
                            NoteBlockInstrument.entries.random(),
                            Random.nextFloat()
                        )
                    ),
                    Random.nextInt(1, 5)
                )
            }

            val musicInstructions = MusicSheetItemComponent(beats)

            val musicStack = ModItems.MUSIC_SHEET.toStack()
            musicStack.set(MusicSheetItemComponent.component, musicInstructions)

            return musicStack
        }

        fun toggleRecording(stack: ItemStack, level: Level) {
            if (stack.has(LongItemComponent.startedRecordingAt)) {
                stack.remove(LongItemComponent.startedRecordingAt)
            } else {
                stack.set(LongItemComponent.startedRecordingAt, LongItemComponent(level.gameTime))
            }
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        if (pPlayer.isCrouching) {
            toggleRecording(pPlayer.getItemInHand(pUsedHand), pLevel)
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand))
        }

        val newMusicStack = createRandomMusicSheet()
        pPlayer.setItemInHand(pUsedHand, newMusicStack)
        playSounds(newMusicStack, pPlayer.blockPosition().above(3))

        return InteractionResultHolder.success(newMusicStack)
    }

}
