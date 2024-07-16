package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ListSongsCommand {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("list")
            .executes(::listSongs)
    }

    private fun listSongs(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.entity ?: return 0

        val songSavedData = SongSavedData.get(player)
        val songs = songSavedData.getSongInfosGroupedByAuthor()

        player.sendSystemMessage(ModLanguageProvider.Message.SONGS_LIST.toComponent())
        for (songInfo in songs) {
            val component = songInfo.getComponent()

            player.sendSystemMessage(component)
        }

        return 1
    }

}