package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData.Companion.songData
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

        val songSavedData = player.server.songData
        val songs = songSavedData.getSongInfosGroupedByAuthor()

        player.sendSystemMessage(ModLanguageProvider.Message.SONGS_LIST.toComponent())
        for (songInfo in songs) {
            val component = songInfo.getComponent()

            player.sendSystemMessage(component)
        }

        return 1
    }

}