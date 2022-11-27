package com.aaronhowser1.pitchperfect.event;

import com.aaronhowser1.pitchperfect.PitchPerfect;
import com.aaronhowser1.pitchperfect.utils.ServerUtils;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PitchPerfect.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {

            //If attacking with an instrument
            if (attacker.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
                instrumentItem.attack(target);
            }

            //If attacking while an instrument with Electric enchant is in your inventory, regardless of location
            boolean hasElectricEnchant = false;
            if (attacker instanceof Player player) {
                for (ItemStack itemStack : player.getInventory().items) {
                    if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get(), itemStack) != 0) {
                        hasElectricEnchant = true;
                        break;
                    }
                }
            } else {
                for (ItemStack itemStack : attacker.getAllSlots()) {
                    if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get(), itemStack) != 0) {
                        hasElectricEnchant = true;
                        break;
                    }
                }
            }
            if (hasElectricEnchant) {
                if (!attacker.getLevel().isClientSide()) {

                    List<LivingEntity> entitiesHit = new ArrayList<>();
                    entitiesHit.add(target);
                    entitiesHit.add(attacker);

                    //TODO: If instanceof Monster, don't spread to Animal

                    //Particle line from initial target to first mob hit by enchant effect
                    if (!ServerUtils.getNearbyLivingEntities(target, CommonConfigs.ELECTRIC_RANGE.get()).isEmpty()) {
                        List<LivingEntity> nearbyLiving = ServerUtils.getNearbyLivingEntities(target, CommonConfigs.ELECTRIC_RANGE.get());
                        nearbyLiving.removeAll(entitiesHit);
                        if (!nearbyLiving.isEmpty()) {
                            LivingEntity closestEntity = ServerUtils.getNearestEntity(nearbyLiving,target);
                            ServerUtils.spawnElectricParticleLine(
                                    new Vec3(target.getX(),target.getY(),target.getZ()),
                                    new Vec3(closestEntity.getX(),closestEntity.getY(), closestEntity.getZ()),
                                    (ServerLevel) closestEntity.getLevel()
                            );

                            //Wait for the particles to reach
                            Util.backgroundExecutor().submit( () -> {
                                try {
                                    Thread.sleep(CommonConfigs.ELECTRIC_JUMPTIME.get());
                                } catch (Exception ignored) {
                                }
                                if (attacker instanceof Player player && player.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
                                    AndHisMusicWasElectricEnchantment.damage(target, closestEntity, entitiesHit, 1, event, instrumentItem);
                                } else {
                                    AndHisMusicWasElectricEnchantment.damage(target, closestEntity, entitiesHit, 1, event);
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
