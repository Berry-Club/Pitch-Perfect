package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.config.ServerConfigs;
import com.aaronhowser1.pitchperfect.utils.ServerUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.ForgeRegistries;

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

       List<LivingEntity> nearbyMobs = ServerUtils.getNearbyLivingEntities(user, (int) user.getBoundingBox().getSize());

       List<LivingEntity> mobsToHeal = getMobsToHeal(nearbyMobs);

       return mobsToHeal;
    }

    public static void heal(LivingEntity target) {
        target.heal(CommonConfigs.HEAL_AMOUNT.get());
    }

    private static boolean canBeHealed(LivingEntity target) {
        return target.getHealth() < target.getMaxHealth();
    }

    private static boolean isMonster(LivingEntity target) {
        return target instanceof Monster;
    }


    private static List<LivingEntity> getMobsToHeal(List<LivingEntity> livingEntityList) {
        List<? extends String> whitelist = ServerConfigs.HEALING_BEAT_WHITELIST.get();
        List<? extends String> blacklist = ServerConfigs.HEALING_BEAT_BLACKLIST.get();

        return livingEntityList.stream().filter(mob -> {

            EntityType<?> entityType = mob.getType();
            ResourceLocation resourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(entityType);

            assert resourceLocation != null;
            boolean mobInWhitelist = whitelist.contains(resourceLocation.toString());
            boolean mobInBlacklist = blacklist.contains(resourceLocation.toString());

            return ((!isMonster(mob) || mobInWhitelist) && !mobInBlacklist && canBeHealed(mob));

        }).toList();

    }

}
