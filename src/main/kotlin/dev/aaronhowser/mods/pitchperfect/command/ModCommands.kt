package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.CommandDispatcher
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ModCommands {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {

        val modCommands = dispatcher.register(
            Commands
                .literal(PitchPerfect.ID)
                .then(PlayRawSongCommand.register())
                .then(PlaySongCommand.register())
                .then(ListSongsCommand.register())
                .then(RemoveSongCommand.register())
                .then(PasteSongCommand.register())
        )

    }

}