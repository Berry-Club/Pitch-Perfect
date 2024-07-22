package dev.aaronhowser.mods.pitchperfect.packet.client_to_server

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.network.handling.IPayloadContext

class ComposerPasteSongPacket(
    val songString: String,
    val composerPos: BlockPos
) : IModPacket {

    constructor(song: Song, composerPos: BlockPos) : this(song.toString(), composerPos)

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()

            val song = Song.fromString(songString)
            if (song == null) {
                context.player().sendSystemMessage(Component.literal("Failed to parse song"))
                return@enqueueWork
            }

            val composerBlockEntity = player.level().getBlockEntity(composerPos) as? ComposerBlockEntity
                ?: return@enqueueWork

            val playerReach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)
            if (!player.canInteractWithBlock(composerPos, playerReach)) return@enqueueWork

            composerBlockEntity.setSong(song)
        }
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<ComposerPasteSongPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("composer_paste_song"))

        val STREAM_CODEC: StreamCodec<ByteBuf, ComposerPasteSongPacket> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ComposerPasteSongPacket::songString,
                BlockPos.STREAM_CODEC, ComposerPasteSongPacket::composerPos,
                ::ComposerPasteSongPacket
            )
    }

}