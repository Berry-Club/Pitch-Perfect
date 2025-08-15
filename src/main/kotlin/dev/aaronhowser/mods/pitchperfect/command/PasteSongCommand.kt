package dev.aaronhowser.mods.pitchperfect.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SongPasteCommandRequestPacket
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer

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

	private fun sendPastePacket(context: CommandContext<CommandSourceStack>): Int {
		val title = StringArgumentType.getString(context, TITLE_ARGUMENT)
		val player = context.source.entity as? ServerPlayer ?: return 0

		ModPacketHandler.messagePlayer(player, SongPasteCommandRequestPacket(title))

		return 1
	}

}