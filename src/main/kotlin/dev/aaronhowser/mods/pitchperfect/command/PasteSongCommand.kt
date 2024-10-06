package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.SongPasteCommandResponsePacket
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SongPasteCommandRequestPacket
import dev.aaronhowser.mods.pitchperfect.song.data.SongSavedData.Companion.songData
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

object PasteSongCommand {

    private const val TITLE_ARGUMENT = "title"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("pasteSong")
            .then(
                Commands
                    .argument(TITLE_ARGUMENT, StringArgumentType.string())
                    .executes(::sendPastePacket)
            )
    }

    fun receivePacket(packet: SongPasteCommandResponsePacket, context: IPayloadContext) {
        val player = context.player() as ServerPlayer
        val song = Song.fromString(packet.clipboard)

        if (song == null) {
            player.sendSystemMessage(ModLanguageProvider.Message.SONG_PASTE_FAIL_TO_PARSE.toComponent(packet.clipboard))
            return
        }

        val title = packet.title

        val songInfo = SongInfo(
            packet.title,
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

    }

    private fun sendPastePacket(context: CommandContext<CommandSourceStack>): Int {
        val title = StringArgumentType.getString(context, TITLE_ARGUMENT)
        val player = context.source.entity as? ServerPlayer ?: return 0

        ModPacketHandler.messagePlayer(player, SongPasteCommandRequestPacket(title))

        return 1
    }

}