package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModMessageLang
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.song.SongBuilder
import dev.aaronhowser.mods.pitchperfect.song.SongPlayer
import dev.aaronhowser.mods.pitchperfect.song.SongRecorder
import dev.aaronhowser.mods.pitchperfect.song.data.SongSavedData.Companion.songData
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
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

	//TODO: When used on a Composer, it should open a menu asking you to title the song, and then saves the song and sets the Sheet Music to it

	override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val stack = pPlayer.getItemInHand(pUsedHand)

		if (pPlayer !is ServerPlayer) return InteractionResultHolder.pass(stack)

		if (pPlayer.isShiftKeyDown) {
			toggleRecording(stack, pPlayer)
			return InteractionResultHolder.success(stack)
		}

		val song = SongBuilder.fromFile(Song.defaultFile) ?: return InteractionResultHolder.fail(stack)
		playSong(song, pPlayer)

		return InteractionResultHolder.success(stack)
	}

	override fun isFoil(pStack: ItemStack): Boolean {
		return isRecording(pStack)
	}

	companion object {

		fun playSong(song: Song, player: Player) {
			val serverLevel = player.level() as ServerLevel

			val songPlayer = SongPlayer(
				serverLevel,
				song
			) { player.eyePosition }

			songPlayer.startPlaying()
		}

		fun toggleRecording(stack: ItemStack, player: ServerPlayer) {
			if (isRecording(stack)) {
				stopRecording(stack, player)
			} else {
				stack.set(ModDataComponents.IS_RECORDING, true)
				stack.remove(ModDataComponents.SONG_UUID)
			}
		}

		private fun stopRecording(itemStack: ItemStack, player: ServerPlayer) {
			itemStack.remove(ModDataComponents.IS_RECORDING)

			val songBuilder = SongRecorder.SONG_RECORDERS[player] ?: return
			SongRecorder.SONG_RECORDERS.remove(player)
			val song = songBuilder.build(Song.defaultFile)

			val songData = player.server.songData
			val addSongResult = songData.addSongInfo(song, "Untitled", player)

			if (!addSongResult.success) {
				player.sendSystemMessage(
					ModMessageLang.SHEET_MUSIC_FAIL_DUPLICATE
						.toComponent(addSongResult.songInfo.getComponent())
				)
				return
			}

			itemStack.set(
				ModDataComponents.SONG_UUID,
				addSongResult.songInfo.song.uuid
			)
		}

		fun isRecording(stack: ItemStack): Boolean {
			return stack.getOrDefault(ModDataComponents.IS_RECORDING, false)
		}
	}

}
