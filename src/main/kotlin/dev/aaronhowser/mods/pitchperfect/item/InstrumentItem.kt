package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.config.CommonConfig
import dev.aaronhowser.mods.pitchperfect.enchantment.BwaaapEnchantment
import dev.aaronhowser.mods.pitchperfect.enchantment.HealingBeatEnchantment
import dev.aaronhowser.mods.pitchperfect.enchantment.ModEnchantments
import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class InstrumentItem(
    instrument: InstrumentComponent.Instrument
) : Item(
    Properties()
        .durability(100)
        .component(InstrumentComponent.component, InstrumentComponent(instrument))
        .attributes(
            ItemAttributeModifiers.builder()
                .add(
                    Attributes.ATTACK_DAMAGE,
                    AttributeModifier(
                        BASE_ATTACK_DAMAGE_ID,
                        0.1,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
                )
                .build()
        )
) {

    companion object {

        fun getInstrument(itemStack: ItemStack): InstrumentComponent? {
            return itemStack.get(InstrumentComponent.component)
        }

        fun getSoundEvent(itemStack: ItemStack): SoundEvent? {
            val instrument = getInstrument(itemStack) ?: return null
            return instrument.soundEvent
        }

        fun healingBeat(itemStack: ItemStack, player: Player) {
            if (player.cooldowns.isOnCooldown(itemStack.item)) return

            val healingBeatLevel = itemStack.getEnchantmentLevel(
                ModEnchantments.getEnchantHolder(player.level(), ModEnchantments.healingBeatResourceKey)
            )

            if (healingBeatLevel == 0) return

            HealingBeatEnchantment.trigger(player, itemStack)
        }

        fun bwaaap(itemStack: ItemStack, player: Player) {
            if (player.cooldowns.isOnCooldown(itemStack.item)) return

            val bwaaapLevel = itemStack.getEnchantmentLevel(
                ModEnchantments.getEnchantHolder(player.level(), ModEnchantments.bwaaapResourceKey)
            )

            if (bwaaapLevel == 0) return

            BwaaapEnchantment.trigger(player, itemStack)
        }

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
        val sound = getSoundEvent(itemStack) ?: return

        val lookVector = player.lookAngle
        val pitch = lookVector.y.toFloat().map(-1f, 1f, 0.5f, 2f)

        val noteVector = if (interactionHand == InteractionHand.MAIN_HAND) {
            lookVector.yRot(-0.5f)
        } else {
            lookVector.yRot(0.5f)
        }

        if (!level.isClientSide) {
            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    sound.location,
                    pitch,
                    (player.x + noteVector.x),
                    (player.eyeY + noteVector.y),
                    (player.z + noteVector.z),
                    false   //TODO BWAAAAP
                ),
                level as ServerLevel,
                player.eyePosition,
                128.0
            )
        }

        healingBeat(itemStack, player)
        bwaaap(itemStack, player)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: Player, entity: Entity): Boolean {
        if (entity.level().isClientSide) return false

        val sound = getSoundEvent(stack) ?: return false

        val particleAmountLowerBound = CommonConfig.MIN_ATTACK_PARTICLES.get()
        val particleAmountUpperBound = CommonConfig.MAX_ATTACK_PARTICLES.get()

        require(particleAmountLowerBound <= particleAmountUpperBound) {
            "Min attack particles cannot be greater than max attack particles."
        }

        val range = particleAmountLowerBound..particleAmountUpperBound
        val randomAmount = range.random()

        val entityWidth = entity.bbWidth
        val entityHeight = entity.bbHeight

        for (note in 1..randomAmount) {
            val randomPitch = Random.nextDouble(0.5, 2.0).toFloat()

            val noteX = entity.x + entityWidth * Random.nextDouble(-1.5, 1.5)
            val noteZ = entity.z + entityWidth * Random.nextDouble(-1.5, 1.5)
            val noteY = entity.y + entityHeight + entityHeight * Random.nextDouble(-0.75, 0.75)

            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    sound.location,
                    randomPitch,
                    noteX,
                    noteY,
                    noteZ
                ),
                entity.level() as ServerLevel,
                Vec3(entity.x, entity.y, entity.z),
                48.0
            )
        }

        return false
    }

}