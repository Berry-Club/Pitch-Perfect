package dev.aaronhowser.pitchperfect.item

import dev.aaronhowser.pitchperfect.config.CommonConfig
import dev.aaronhowser.pitchperfect.enchantment.BwaaapEnchantment
import dev.aaronhowser.pitchperfect.enchantment.HealingBeatEnchantment
import dev.aaronhowser.pitchperfect.enchantment.ModEnchantments
import dev.aaronhowser.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.pitchperfect.packet.SpawnNotePacket
import dev.aaronhowser.pitchperfect.utils.CommonUtils.hasEnchantment
import dev.aaronhowser.pitchperfect.utils.CommonUtils.map
import com.google.common.collect.HashMultimap
import com.google.common.collect.ImmutableSetMultimap
import com.google.common.collect.Multimap
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.util.Lazy
import kotlin.random.Random

class InstrumentItem(
    val sound: SoundEvent
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

        val hasBwaaap = itemStack.hasEnchantment(ModEnchantments.BWAAAP.get())

        if (!level.isClientSide()) {
            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    sound.location,
                    pitch,
                    (player.x + noteVector.x()),
                    (player.eyeY + noteVector.y()),
                    (player.z + noteVector.z()),
                    hasBwaaap
                ),
                player.getLevel() as ServerLevel,
                Vec3(player.x, player.y, player.z),
                128.0
            )
        }
    }

    private fun bwaaap(itemStack: ItemStack, player: Player) {
        if (EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.BWAAAP.get(), itemStack) == 0) return

        BwaaapEnchantment.triggerBwaaap(player, this)
    }

    private fun healingBeat(itemStack: ItemStack, player: Player) {
        if (EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.HEALING_BEAT.get(), itemStack) == 0) return

        HealingBeatEnchantment.heal(player, itemStack)
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

            val randomPitch = Random.nextDouble(0.5, 2.0).toFloat()

            val noteX = target.x + entityWidth * Random.nextDouble(-1.5, 1.5)
            val noteZ = target.z + entityWidth * Random.nextDouble(-1.5, 1.5)
            val noteY = target.y + entityHeight + (entityHeight * Random.nextDouble(-0.75, 0.75))

            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    sound.location,
                    randomPitch,
                    noteX,
                    noteY,
                    noteZ
                ),
                target.getLevel() as ServerLevel,
                Vec3(noteX, noteY, noteZ),
                16.0
            )
        }
    }

    override fun isEnchantable(pStack: ItemStack): Boolean = true

    override fun getEnchantmentValue(stack: ItemStack): Int = 1

    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment): Boolean {
        return enchantment.category == ModEnchantments.INSTRUMENT
    }

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