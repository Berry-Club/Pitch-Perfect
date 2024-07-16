package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.UuidArgument

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

        player.sendSystemMessage(
            ModLanguageProvider.Message.SONG_REMOVED.toComponent(
                songInfo.title,
                songInfo.authorName
            )
        )

        return 1
    }

}