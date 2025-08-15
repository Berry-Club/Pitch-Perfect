package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModMessageLang
import dev.aaronhowser.mods.pitchperfect.song.data.SongSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer

object ListSongsCommand {

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("list")
			.executes(::listSongs)
	}

	private fun listSongs(context: CommandContext<CommandSourceStack>): Int {
		val player = context.source.entity as? ServerPlayer ?: return 0

		val songSavedData = SongSavedData.get(player.serverLevel())
		val songs = songSavedData.getSavedSongsGroupedByAuthor()

		player.sendSystemMessage(ModMessageLang.SONGS_LIST.toComponent())
		for (songInfo in songs) {
			val component = songInfo.getComponent()

			player.sendSystemMessage(component)
		}

		return 1
	}

}