package com.aaronhowser1.pitchperfect.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.ImmutableSetMultimap
import com.google.common.collect.Multimap
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.util.Lazy

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


    override fun isEnchantable(pStack: ItemStack): Boolean = true

    override fun getEnchantmentValue(stack: ItemStack?): Int = 1

//    override fun canApplyAtEnchantingTable(stack: ItemStack?, enchantment: Enchantment): Boolean {
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

    override fun getAttributeModifiers(slot: EquipmentSlot, stack: ItemStack?): Multimap<Attribute, AttributeModifier> {
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