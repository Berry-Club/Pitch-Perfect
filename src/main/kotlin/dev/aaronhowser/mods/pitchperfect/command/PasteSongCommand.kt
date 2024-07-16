package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.song.SongSavedData.Companion.songData
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.client.Minecraft
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer

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
            val player = context.source.entity as? ServerPlayer ?: return 0

            //TODO: See if this works on servers
            val clipboard = Minecraft.getInstance().keyboardHandler.clipboard

            val song = Song.fromString(clipboard)

            if (song == null) {
                player.sendSystemMessage(ModLanguageProvider.Message.SONG_PASTE_FAIL_TO_PARSE.toComponent(clipboard))
                return 0
            }

            val songInfo = SongInfo(
                title,
                player,
                song
            )

            val songSavedData = player.server.songData
            val result = songSavedData.addSongInfo(songInfo)

            val component = if (result.success) {
                ModLanguageProvider.Message.SONG_PASTE_ADDED
                    .toComponent(title)
                    .withStyle {
                        it
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    ModLanguageProvider.Message.CLICK_COPY_SONG_UUID.toComponent(song.uuid.toString())
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
                ModLanguageProvider.Message.SONG_PASTE_FAIL_DUPLICATE.toComponent(title)
                    .append(result.songInfo.getComponent())
            }

            player.sendSystemMessage(component)

            return if (result.success) 1 else 0
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

}