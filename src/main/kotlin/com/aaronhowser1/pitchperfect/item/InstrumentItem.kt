package com.aaronhowser1.pitchperfect.item

import com.aaronhowser1.pitchperfect.config.ClientConfig
import com.aaronhowser1.pitchperfect.config.CommonConfig
import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.enchantment.BwaaapEnchantment
import com.aaronhowser1.pitchperfect.enchantment.HealingBeatEnchantment
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnNoteParticlePacket
import com.aaronhowser1.pitchperfect.utils.CommonUtils
import com.aaronhowser1.pitchperfect.utils.CommonUtils.hasEnchantment
import com.google.common.collect.HashMultimap
import com.google.common.collect.ImmutableSetMultimap
import com.google.common.collect.Multimap
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.util.Lazy
import kotlin.math.max

class InstrumentItem(
    private val sound: SoundEvent
) : Item(
    Properties()
        .stacksTo(1)
        .tab(ModCreativeTab.MOD_TAB)
        .defaultDurability(100)
) {

    companion object {
        const val ATTACK_DAMAGE = 0.1
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val itemStack = player.getItemInHand(interactionHand)

        useInstrument(itemStack, player, level, interactionHand)

        //Enchantments
        if (!player.cooldowns.isOnCooldown(this)) {
            healingBeat(itemStack, player)
            bwaaap(itemStack, player)
        }

        return InteractionResultHolder.fail(itemStack)
    }

    private fun useInstrument(itemStack: ItemStack, player: Player, level: Level, interactionHand: InteractionHand) {
        val lookVector = player.lookAngle

        var pitch = lookVector.y().toFloat()

        pitch = CommonUtils.map(pitch, -1f, 1f, 0.5f, 2f)

        if (itemStack.hasEnchantment(ModEnchantments.BWAAAP.get())) {
            playSound(level, pitch, player.x, player.y, player.z, ClientConfig.VOLUME.get().toFloat())
        } else {
            playSound(level, pitch, player.x, player.y, player.z, ClientConfig.VOLUME.get().toFloat())
        }

        val noteVector = if (interactionHand == InteractionHand.MAIN_HAND) {
            lookVector.yRot(-0.5f)
        } else {
            lookVector.yRot(0.5f)
        }

        if (!level.isClientSide()) {
            ModPacketHandler.messageNearbyPlayers(
                SpawnNoteParticlePacket(
                    sound.location,
                    pitch,
                    (player.x + noteVector.x()),
                    (player.eyeY + noteVector.y()),
                    (player.z + noteVector.z())
                ),
                player.getLevel() as ServerLevel,
                Vec3(player.x, player.y, player.z),
                128.0
            )
        }
    }

    private fun bwaaap(itemStack: ItemStack, player: Player) {
        if (EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.BWAAAP.get(), itemStack) == 0) return
        BwaaapEnchantment.knockBack(player)
        player.cooldowns.addCooldown(this, BwaaapEnchantment.getCooldown(player))
    }

    private fun healingBeat(itemStack: ItemStack, player: Player) {
        if (EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.HEALING_BEAT.get(), itemStack) == 0) return
        val healTargets: List<LivingEntity> = HealingBeatEnchantment.getTargets(player)

        for (target in healTargets) {
            HealingBeatEnchantment.heal(target)

            if (!player.level.isClientSide()) {
                ModPacketHandler.messageNearbyPlayers(
                    SpawnNoteParticlePacket(
                        sound.location,
                        player.lookAngle.y.toFloat(),
                        target.x,
                        target.eyeY,
                        target.z
                    ),
                    target.getLevel() as ServerLevel,
                    Vec3(target.x, target.eyeY, target.z),
                    64.0
                )
            }
        }

        player.cooldowns.addCooldown(this, (healTargets.size * ServerConfig.HEAL_COOLDOWN_MULT.get()).toInt())
    }

    fun attack(target: Entity) {

        val particleAmountLowerBound: Int = CommonConfig.MIN_ATTACK_PARTICLES.get()
        val particleAmountUpperBound: Int = CommonConfig.MAX_ATTACK_PARTICLES.get()

        if (particleAmountLowerBound > particleAmountUpperBound) {
            throw IllegalArgumentException("Min attack particles cannot be greater than max attack particles")
        }

        val range = particleAmountLowerBound..particleAmountUpperBound
        val randomAmount = range.random()

        val entityWidth = target.bbWidth.toDouble()
        val entityHeight = target.bbHeight.toDouble()
        for (note in 1..randomAmount) {

            var randomPitch = ((-90..90).random()).toFloat()

            randomPitch = CommonUtils.map(
                randomPitch,
                -90f,
                90f,
                2f,
                0.5f
            ) //from [-90,90] to [2,0.5], high->low bc big number = low pitch

            val noteX = (target.x + entityWidth * (Math.random() * 3 - 1.5)).toFloat()
            val noteZ = (target.z + entityWidth * (Math.random() * 3 - 1.5)).toFloat()
            val noteY = (target.y + entityHeight + (entityHeight * Math.random() * 1.5 - .75)).toFloat()

//                ModPacketHandler.messageNearbyPlayers(
//                    SpawnNoteParticlePacket(sound.location, ServerLevel, noteX, noteY, noteZ),
//                    target.getLevel() as ServerLevel,
//                    Vec3(noteX.toDouble(), noteY.toDouble(), noteZ.toDouble()),
//                    16
//                )

            //TODO: Don't use ClientConfig, possibly use packets instead
            playSound(
                target.getLevel(),
                randomPitch,
                noteX.toDouble(),
                noteY.toDouble(),
                noteZ.toDouble(),
                max(
                    ClientConfig.VOLUME.get().toFloat() / randomAmount,
                    ClientConfig.MIN_ATTACK_VOLUME.get().toFloat()
                )
            )
        }
    }

    private fun playSound(level: Level, pitch: Float, x: Double, y: Double, z: Double, volume: Float) {
        level.playSound(
            null,
            BlockPos(x, y, z),
            sound,
            SoundSource.PLAYERS,
            volume,
            pitch
        )
    }

    override fun isEnchantable(pStack: ItemStack): Boolean = true

    override fun getEnchantmentValue(stack: ItemStack): Int = 1

//    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment): Boolean {
//        return enchantment.category == ModEnchantments.INSTRUMENT
//    }

    //Ok beyond this, I barely understood
    //Mostly stolen from https://github.com/Tslat/Advent-Of-Ascension/blob/1.18.2/source/content/item/weapon/maul/BaseMaul.java (with permission, thanks Tslat)
    private val attributeModifiers: Lazy<ImmutableSetMultimap<Attribute, AttributeModifier>> = buildDefaultAttributes()

    private fun buildDefaultAttributes(): Lazy<ImmutableSetMultimap<Attribute, AttributeModifier>> {
        return Lazy.of {
            ImmutableSetMultimap.of(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(
                    BASE_ATTACK_DAMAGE_UUID,
                    "Weapon Modifier",
                    ATTACK_DAMAGE,
                    AttributeModifier.Operation.ADDITION
                )
            )
        }
    }

    override fun getAttributeModifiers(slot: EquipmentSlot, stack: ItemStack): Multimap<Attribute, AttributeModifier> {
        //If and only if item is in the hand, for some reason
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack)

        //Make a new map
        val newMap: Multimap<Attribute, AttributeModifier> = HashMultimap.create()
        //Get the existing one, made by the Lazy constructor
        val attributes: ImmutableSetMultimap<Attribute, AttributeModifier> = attributeModifiers.get()

        //GET EVERY VALUE OF THE ORIGINAL and put it in the new one
        for ((key, value) in attributes.entries()) {
            newMap.put(key, value)
        }

        //Return the new one
        return newMap
        //This is so fucking stupid
    }

}