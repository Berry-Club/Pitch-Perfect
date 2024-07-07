package dev.aaronhowser.mods.pitchperfect.item

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mojang.serialization.JsonOps
import dev.aaronhowser.mods.pitchperfect.event.OtherEvents
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanItemComponent.Companion.isTrue
import dev.aaronhowser.mods.pitchperfect.serialization.LatvianWhy
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Files

class SheetMusicItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        val GSON: Gson = GsonBuilder().setPrettyPrinting().setLenient().serializeNulls().disableHtmlEscaping().create()

        fun playSounds(song: LatvianWhy.Song, player: Player) {
            println(song)

            val jsonString = GSON.toJson(LatvianWhy.Song.CODEC.encodeStart(JsonOps.INSTANCE, song).getOrThrow())

            Files.writeString(FMLPaths.CONFIGDIR.get().resolve("my_song.json"), jsonString)
        }

        fun toggleRecording(stack: ItemStack, player: Player) {
            if (isRecording(stack)) {
                stopRecording(stack, player)
            } else {
                stack.set(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(true))
            }
        }

        private fun stopRecording(itemStack: ItemStack, player: Player) {
            itemStack.remove(BooleanItemComponent.isRecordingComponent)

            val songBuilder = OtherEvents.builders[player] ?: return
            OtherEvents.builders.remove(player)
            val song = songBuilder.build()

            playSounds(song, player)
        }

        fun isRecording(stack: ItemStack): Boolean {
            return stack.get(BooleanItemComponent.isRecordingComponent).isTrue
        }
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = pPlayer.getItemInHand(pUsedHand)

        if (pLevel.isClientSide) return InteractionResultHolder.pass(stack)

        if (pPlayer.isShiftKeyDown) {
            toggleRecording(stack, pPlayer)
            return InteractionResultHolder.success(stack)
        }

//        playSounds(stack, pPlayer)

        return InteractionResultHolder.success(stack)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

}
