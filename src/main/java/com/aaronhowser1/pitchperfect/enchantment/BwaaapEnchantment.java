package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BwaaapEnchantment extends Enchantment {

    private static int range = CommonConfigs.BWAAAP_RANGE.get();

    public BwaaapEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMinCost(int pLevel) {
        return 5;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return 25;
    }

    public static List<LivingEntity> getTargets(LivingEntity user) {
        BlockPos userLocation = user.blockPosition();
        return user.getLevel().getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat(),
                user,
                new AABB(
                        userLocation.offset(-range,-range,-range),
                        userLocation.offset(range,range,range)
                )
        );
    }

    public static void knockback(LivingEntity user) {
        getTargets(user).forEach(target -> {
            int range = CommonConfigs.BWAAAP_RANGE.get();
            float strength = CommonConfigs.BWAAAP_STRENGTH.get();

            Vec3 targetMotion = target.getDeltaMovement();
            Vec3 userToTargetVector = new Vec3(
                    target.getX()-user.getX(),
                    target.getY()-user.getY(),
                    target.getZ()-user.getZ()
            );
            double distanceToTarget = userToTargetVector.length();
            double targetPercentageFromRange = Math.abs(distanceToTarget/range - 1);

            target.setDeltaMovement(
                    targetMotion.add(
                            userToTargetVector.multiply(
                                    targetPercentageFromRange*strength,
                                    targetPercentageFromRange*strength,
                                    targetPercentageFromRange*strength
                            )
                    )
            );
        });
    }


}
