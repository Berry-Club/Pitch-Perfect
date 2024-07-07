package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.item.component.MusicItemComponent
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
        .component(MusicItemComponent.component, MusicItemComponent(emptyList()))
) {

    companion object {

        fun playSounds(musicStack: ItemStack, blockPos: BlockPos) {
            val musicInstructions = musicStack.get(MusicItemComponent.component) ?: return

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
                MusicItemComponent.Beat(
                    listOf(
                        MusicItemComponent.Beat.Doot(
                            NoteBlockInstrument.entries.random(),
                            Random.nextFloat()
                        )
                    ),
                    Random.nextInt(1, 5)
                )
            }

            val musicInstructions = MusicItemComponent(beats)

            val musicStack = ModItems.MUSIC_SHEET.toStack()
            musicStack.set(MusicItemComponent.component, musicInstructions)

            return musicStack
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val newMusicStack = createRandomMusicSheet()
        pPlayer.setItemInHand(pUsedHand, newMusicStack)
        playSounds(newMusicStack, pPlayer.blockPosition().above(3))

        return InteractionResultHolder.success(newMusicStack)
    }

}
