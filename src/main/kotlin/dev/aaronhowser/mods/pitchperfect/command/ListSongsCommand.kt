package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object ListSongsCommand {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("list")
            .executes(::listSongs)
    }

    private fun listSongs(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.entity ?: return 0

        val overworld = context.source.server.overworld()
        val songSavedData = SongSavedData.get(overworld)

        val songs = songSavedData.getSongsGroupedByAuthor()

        player.sendSystemMessage(Component.literal("Songs:"))
        for (songInfo in songs) {
            val component = songInfo.getComponent()

            player.sendSystemMessage(component)
        }

        return 1
    }

}