package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.event.OtherEvents
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent.Companion.isTrue
import dev.aaronhowser.mods.pitchperfect.song.SongPlayer
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SheetMusicItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        fun playSong(song: Song, player: Player) {
            val serverLevel = player.level() as ServerLevel

            val songPlayer = SongPlayer(
                serverLevel,
                song
            ) { player.eyePosition }

            songPlayer.startPlaying()
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
            songBuilder.build(Song.defaultFile)
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

        val song =
            Song.fromFile(Song.defaultFile) ?: return InteractionResultHolder.fail(stack)
        playSong(song, pPlayer)

        return InteractionResultHolder.success(stack)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

}
