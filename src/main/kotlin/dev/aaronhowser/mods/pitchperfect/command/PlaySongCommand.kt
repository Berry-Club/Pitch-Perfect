package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.song.SongPlayer
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.UuidArgument
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

object PlaySongCommand {

    private const val SONG_ARGUMENT = "song"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("playSong")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(SONG_ARGUMENT, UuidArgument.uuid())
                    .executes(::playSong)
            )
    }

    private fun playSong(context: CommandContext<CommandSourceStack>): Int {
        try {
            val songUuid = UuidArgument.getUuid(context, SONG_ARGUMENT)
            val player = context.source.entity as? ServerPlayer ?: return 0

            val songSavedData = SongSavedData.get(player)
            val songInfo = songSavedData.getSongInfo(songUuid) ?: return 0

            val songPlayer = SongPlayer(player.level() as ServerLevel, songInfo.song) { player.eyePosition }

            songPlayer.startPlaying()
            return 1
        } catch (e: IllegalArgumentException) {
            return 0
        }
    }

}