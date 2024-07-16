package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.song.SongPlayer
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

object PlayRawSongCommand {

    private const val SONG_ARGUMENT = "song"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("playRawSong")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(SONG_ARGUMENT, StringArgumentType.greedyString())
                    .executes(::playSong)
            )
    }

    private fun playSong(context: CommandContext<CommandSourceStack>): Int {
        try {
            val songString = StringArgumentType.getString(context, SONG_ARGUMENT)
            val song = Song.fromString(songString)

            val player = context.source.entity as? ServerPlayer ?: return 0

            if (song == null) {
                player.sendSystemMessage(
                    ModLanguageProvider.Message.SONG_RAW_FAIL_TO_PARSE.toComponent(songString),
                    false
                )
                return 0
            }

            val songPlayer = SongPlayer(player.level() as ServerLevel, song) { player.eyePosition }

            songPlayer.startPlaying()
            return 1
        } catch (e: IllegalArgumentException) {
            return 0
        }
    }

}