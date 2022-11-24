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

    //Shakes screen and throws away mobs
    //Cooldown?
    //Apparently has a shader, https://i.latvian.dev/pc/2022-11-20_12.09.52.mp4, can remove rgb

    //Maybe override the sound and use https://www.youtube.com/watch?v=rZB5BbvrRgA&t=113s, didgeridoo specific

    private static int range;

    public BwaaapEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(Enchantment.Rarity.COMMON, ModEnchantments.INSTRUMENT, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
        this.range = 5;
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
