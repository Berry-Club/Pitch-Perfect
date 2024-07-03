package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class InstrumentItem(
    val sound: Holder.Reference<SoundEvent>
) : Item(
    Properties()
        .durability(100)
) {

    companion object {


    }

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pUsedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val itemStack = pPlayer.getItemInHand(pUsedHand)

        useInstrument(itemStack, pPlayer, pLevel, pUsedHand)

        return InteractionResultHolder.pass(itemStack)
    }

    private fun useInstrument(
        itemStack: ItemStack,
        player: Player,
        level: Level,
        interactionHand: InteractionHand
    ) {

        val lookVector = player.lookAngle
        val pitch = lookVector.y.toFloat().map(-1f, 1f, 0.5f, 2f)

        val noteVector = if (interactionHand == InteractionHand.MAIN_HAND) {
            lookVector.yRot(-0.5f)
        } else {
            lookVector.yRot(0.5f)
        }

        if (!level.isClientSide) {
            level.playSound(
                null,
                player.blockPosition(),
                sound.value(),
                player.soundSource,
                1f,
                pitch
            )
        }

    }

}