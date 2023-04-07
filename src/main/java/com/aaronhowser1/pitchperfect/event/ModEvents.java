package com.aaronhowser1.pitchperfect.event;

import com.aaronhowser1.pitchperfect.PitchPerfect;
import com.aaronhowser1.pitchperfect.utils.ModScheduler;
import com.aaronhowser1.pitchperfect.utils.ServerUtils;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PitchPerfect.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {

            //If attacking with an instrument
            if (attacker.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
                instrumentItem.attack(target);
            }


            //Sets to the first InstrumentItem that has the enchantment in your inventory, or stays null
            ItemStack electricItemStack = null;
            if (attacker instanceof Player player) {
                for (ItemStack itemStack : player.getInventory().items) {
                    if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get(), itemStack) != 0) {
                        electricItemStack = itemStack;
                        break;
                    }
                }
            } else {
                for (ItemStack itemStack : attacker.getAllSlots()) {
                    if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get(), itemStack) != 0) {
                        electricItemStack = itemStack;
                        break;
                    }
                }
            }
            if (electricItemStack != null) {
                if (!attacker.getLevel().isClientSide()) {

                    ItemStack finalElectricItemStack = electricItemStack;
                    electricItemStack.hurtAndBreak(1, attacker, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));

                    List<LivingEntity> entitiesHit = new ArrayList<>();
                    entitiesHit.add(target);
                    entitiesHit.add(attacker);


                    List<LivingEntity> nearbyLiving = ServerUtils.getNearbyLivingEntities(target, CommonConfigs.ELECTRIC_RANGE.get());
                    nearbyLiving.removeAll(entitiesHit);

                    // God forgive me for what I've created
                    List<String> extraWhatevers = new ArrayList<>();
                    if (target instanceof Monster) {
                        nearbyLiving.removeIf(livingEntity -> !(livingEntity instanceof Monster));
                        extraWhatevers.add("target = monster");
                    }
                    if (attacker instanceof Monster) {
                        nearbyLiving.removeIf(livingEntity -> livingEntity instanceof Monster);
                        extraWhatevers.add("attacker = monster");
                    }

                    if (!nearbyLiving.isEmpty()) {
                        LivingEntity closestEntity = ServerUtils.getNearestEntity(nearbyLiving,target);
                        ServerUtils.spawnElectricParticleLine(
                                new Vec3(target.getX(),target.getY(),target.getZ()),
                                new Vec3(closestEntity.getX(),closestEntity.getY(), closestEntity.getZ()),
                                (ServerLevel) closestEntity.getLevel()
                        );
                        //Wait for the particles to reach
                        ModScheduler.scheduleSynchronisedTask(
                                () -> {
                                    if (attacker instanceof Player player && player.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
                                        AndHisMusicWasElectricEnchantment.damage(target, closestEntity, entitiesHit, 1, event, extraWhatevers, instrumentItem);
                                    } else {
                                        AndHisMusicWasElectricEnchantment.damage(target, closestEntity, entitiesHit, 1, event, extraWhatevers);
                                    }},
                                CommonConfigs.ELECTRIC_JUMPTIME.get()
                        );
                    }
                }
            }
        }
    }

    //From Tslat
    public static int tick;
    @SubscribeEvent
    public static void serverTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tick++;

            ModScheduler.handleSyncScheduledTasks(tick);
        }
    }
}
