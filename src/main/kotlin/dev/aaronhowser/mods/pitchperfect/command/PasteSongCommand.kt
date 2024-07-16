package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.client.Minecraft
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.entity.player.Player

object PasteSongCommand {

    private const val TITLE_ARGUMENT = "title"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("pasteSong")
            .then(
                Commands
                    .argument(TITLE_ARGUMENT, StringArgumentType.string())
                    .executes(::pasteSong)
            )
    }

    private fun pasteSong(context: CommandContext<CommandSourceStack>): Int {

        try {
            val title = StringArgumentType.getString(context, TITLE_ARGUMENT)
            val player = context.source.entity as? Player ?: return 0

            //TODO: See if this works on servers
            val clipboard = Minecraft.getInstance().keyboardHandler.clipboard

            val song = Song.fromString(clipboard)

            if (song == null) {
                player.sendSystemMessage(Component.literal("Failed to parse song from clipboard:\n$clipboard"))
                return 0
            }

            val songInfo = SongInfo(
                title,
                player,
                song
            )

            val songSavedData = SongSavedData.get(player)
            val newSongInfo = songSavedData.addSongInfo(songInfo)

            val component = if (newSongInfo.success) {
                Component
                    .literal("Song added: $title")
                    .withStyle {
                        it
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.literal("Click to copy UUID")
                                )
                            )
                            .withClickEvent(
                                ClickEvent(
                                    ClickEvent.Action.COPY_TO_CLIPBOARD,
                                    songInfo.song.uuid.toString()
                                )
                            )
                    }
            } else {
                Component.literal("Failed to add song, as an identical song already exists!\n")
                    .append(newSongInfo.songInfo.getComponent())
            }

            player.sendSystemMessage(component)

            return if (newSongInfo.success) 1 else 0
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

}