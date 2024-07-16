package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.UuidArgument
import net.minecraft.network.chat.Component

object RemoveSongCommand {

    private const val SONG_ARGUMENT = "songUuid"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeSong")
            .then(
                Commands
                    .argument(SONG_ARGUMENT, UuidArgument.uuid())
                    .executes(::removeSong)
            )
    }

    private fun removeSong(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.entity ?: return 0
        val songSavedData = SongSavedData.get(player)

        val uuid = UuidArgument.getUuid(context, SONG_ARGUMENT)
        val songInfo = songSavedData.getSongInfo(uuid) ?: return 0
        songSavedData.removeSongInfo(uuid)

        player.sendSystemMessage(Component.literal("Song removed: ${songInfo.title} by ${songInfo.authorName}"))

        return 1
    }

}