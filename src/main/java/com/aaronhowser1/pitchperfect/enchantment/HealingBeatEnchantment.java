package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;

public class HealingBeatEnchantment extends Enchantment {
    public HealingBeatEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMinCost(int pLevel) {
        return 1;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return 20;
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
            if (!(target instanceof Monster) && target.getHealth() < target.getMaxHealth()) {
                target.setHealth(target.getHealth() + CommonConfigs.HEAL_AMOUNT.get());
            }
        });
    }


}
