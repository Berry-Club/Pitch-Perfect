package dev.aaronhowser.mods.pitchperfect.packet.client_to_server

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.song.data.ComposerSongSavedData.Companion.composerSongSavedData
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClickComposerCellPacket(
    val delay: Int,
    val pitch: Int,
    val leftClick: Boolean,
    val selectedInstrument: String,
    val blockPos: BlockPos
) : IModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()

            val composerBlockEntity = player.level().getBlockEntity(blockPos) as? ComposerBlockEntity
                ?: return@enqueueWork

            val playerReach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)
            if (!player.canInteractWithBlock(blockPos, playerReach)) return@enqueueWork

            val composerSongUuid = composerBlockEntity.composerSongUuid

            val composerSongSavedData = player.server!!.composerSongSavedData
            val composerSong = composerSongSavedData.getOrCreateSong(composerSongUuid)

            val note = Note.getFromPitch(pitch)
            val soundHolder = Song.getSoundHolder(selectedInstrument)

            composerSong.apply {
                if (leftClick) {
                    addBeat(pitch, note, soundHolder)
                } else {
                    removeBeat(delay, note, soundHolder)
                }

                addAuthor(player)
            }
        }
    }

    override fun type(): CustomPacketPayload.Type<ClickComposerCellPacket> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<ClickComposerCellPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("click_composer_cell"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ClickComposerCellPacket> =
            StreamCodec.composite(
                ByteBufCodecs.VAR_INT, ClickComposerCellPacket::delay,
                ByteBufCodecs.VAR_INT, ClickComposerCellPacket::pitch,
                ByteBufCodecs.BOOL, ClickComposerCellPacket::leftClick,
                ByteBufCodecs.STRING_UTF8, ClickComposerCellPacket::selectedInstrument,
                BlockPos.STREAM_CODEC, ClickComposerCellPacket::blockPos,
                ::ClickComposerCellPacket
            )

    }

}