package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.utils.ModScheduler;
import com.aaronhowser1.pitchperfect.utils.ServerUtils;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import com.aaronhowser1.pitchperfect.packets.SpawnElectricParticlePacket;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.aaronhowser1.pitchperfect.packets.SpawnElectricPathPacket;
import com.ibm.icu.util.TimeUnit;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AndHisMusicWasElectricEnchantment extends Enchantment {
    protected AndHisMusicWasElectricEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    //iteration starts at 1
    public static void damage(LivingEntity originEntity, LivingEntity targetEntity, List<LivingEntity> entitiesHit, int iteration, LivingHurtEvent event, InstrumentItem... instrumentItems) {

        LivingEntity e = targetEntity;

        if (iteration > CommonConfigs.ELECTRIC_MAX_JUMPS.get()) return;

        if (!e.isAlive()) return;

        //Spawn Particles
        double entityWidth = e.getBbWidth();
        double entityHeight = e.getBbHeight();
        for (int p = 1; p <= Math.min(iteration,5); p++) {
            double X = (e.getX() + entityWidth * (Math.random() * .75 - .375));
            double Z = (e.getZ() + entityWidth * (Math.random() * .75 - .375));
            double Y = (e.getY() + entityHeight + Math.min(2,(iteration*0.05)));
            ModPacketHandler.messageNearbyPlayers(
                    new SpawnElectricParticlePacket(X, Y, Z),
                    (ServerLevel) e.getLevel(),
                    new Vec3(X, Y, Z),
                    16
            );
        }

        //Damage
        float damageFactor = CommonConfigs.ELECTRIC_DAMAGE_RETURNS.get() / iteration;
        e.hurt(
                DamageSource.LIGHTNING_BOLT,
                event.getAmount()*damageFactor
        );
        if (instrumentItems.length != 0) {
            instrumentItems[0].attack(e);
        }
        entitiesHit.add(e);



        //Spawn particle line
        List<LivingEntity> nextEntities = ServerUtils.getNearbyLivingEntities(e, CommonConfigs.ELECTRIC_RANGE.get());
        nextEntities.removeAll(entitiesHit);
        nextEntities.remove(e);
        if (!nextEntities.isEmpty()){
            if (iteration+1 <= CommonConfigs.ELECTRIC_MAX_JUMPS.get()) {
                LivingEntity nextEntity = ServerUtils.getNearestEntity(nextEntities, e);
                if (nextEntity.isAlive()) {
                    ServerUtils.spawnElectricParticleLine(
                            new Vec3(e.getX(),e.getY(),e.getZ()),
                            new Vec3(nextEntity.getX(),nextEntity.getY(),nextEntity.getZ()),
                            (ServerLevel) e.getLevel()
                    );

                    //Wait before continuing
                    ModScheduler.scheduleAsyncTask(
                            damage(e, nextEntity, entitiesHit, iteration+1, event, instrumentItems),
                            CommonConfigs.ELECTRIC_JUMPTIME.get(),
                            TimeUnit.MILLISECOND
                    );
//                    Util.backgroundExecutor().submit( () -> {
//                        try {
//                            Thread.sleep(CommonConfigs.ELECTRIC_JUMPTIME.get());
//                        } catch (Exception ignored) {
//                        }
//
//                        if (nextEntity == null) return;
//
//                        damage(e, nextEntity, entitiesHit, iteration+1, event, instrumentItems);
//                    });
                }
            }
        }
    }

    //TODO: enchantment durability --- enchantment only has a limited amount of uses, and it removes itself when it's done

}
