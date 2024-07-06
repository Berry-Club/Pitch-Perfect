package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
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
import kotlin.random.Random
import com.mojang.datafixers.util.Pair as MojangPair

class MusicSheetItem : Item(Properties().stacksTo(1)) {

    companion object {

        fun playSounds(musicStack: ItemStack, blockPos: BlockPos) {
            val musicInstructions = musicStack.get(MusicItemComponent.component) ?: return

            val beats = musicInstructions.beats

            var currentDelay = 0

            for (beat in beats) {
                for (pair in beat.sounds) {
                    val pitch = pair.first
                    val sound = pair.second.soundEvent.value()

                    ModClientScheduler.scheduleTaskInTicks(currentDelay) {

                        ClientUtil.playNote(
                            sound,
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
                        MojangPair(
                            Random.nextFloat(),
                            InstrumentComponent.Instrument.entries.random()
                        )
                    ),
                    2
                )
            }

            val musicInstructions = MusicItemComponent(beats)

            val musicStack = ModItems.MUSIC_SHEET.toStack()
            musicStack.set(MusicItemComponent.component, musicInstructions)

            return musicStack
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val musicStack = pPlayer.getItemInHand(pUsedHand)

        if (musicStack.has(MusicItemComponent.component)) {
            val blockPos = pPlayer.blockPosition()
            playSounds(musicStack, blockPos)
        } else {
            val newMusicStack = createRandomMusicSheet()
            pPlayer.setItemInHand(pUsedHand, newMusicStack)
        }

        return super.use(pLevel, pPlayer, pUsedHand)
    }

}
