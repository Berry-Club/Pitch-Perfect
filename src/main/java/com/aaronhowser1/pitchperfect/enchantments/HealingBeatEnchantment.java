package com.aaronhowser1.pitchperfect.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;

public class HealingBeatEnchantment extends Enchantment {
    public HealingBeatEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(Rarity.COMMON, ModEnchantments.INSTRUMENT, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    public static List<LivingEntity> getTargets(LivingEntity user) {
       return user.getLevel().getNearbyEntities(
               LivingEntity.class,
               TargetingConditions.forCombat(),
               user,
               user.getBoundingBox().inflate(3)
       );
    }

    public static void heal(LivingEntity user) {
        getTargets(user).forEach(target -> {
            if (target.getHealth() != target.getMaxHealth()) {
                target.setHealth(target.getHealth() + 1);
            }
        });
    }

}
