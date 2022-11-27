package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.client.ServerUtils;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import com.aaronhowser1.pitchperfect.packets.SpawnElectricParticlePacket;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.aaronhowser1.pitchperfect.packets.SpawnElectricPathPacket;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class AndHisMusicWasElectricEnchantment extends Enchantment {
    protected AndHisMusicWasElectricEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    //iteration starts at 1
    public static void damage(LivingEntity originEntity, List<Entity> entitiesHit, int iteration, LivingHurtEvent event,  InstrumentItem... instrumentItems) {

        List<LivingEntity> entities = ServerUtils.getNearbyLivingEntities(originEntity, CommonConfigs.ELECTRIC_RANGE.get());
        entities.removeAll(entitiesHit);

        if (entities.isEmpty()) return;
        if (iteration > CommonConfigs.ELECTRIC_MAX_JUMPS.get()) return;

        LivingEntity e = ServerUtils.getNearestEntity(entities, originEntity);
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
            List<LivingEntity> nextEntities = ServerUtils.getNearbyLivingEntities(originEntity, CommonConfigs.ELECTRIC_RANGE.get());
            nextEntities.removeAll(entitiesHit);
            nextEntities.remove(e);
            if (!nextEntities.isEmpty()){
                if (iteration <= CommonConfigs.ELECTRIC_MAX_JUMPS.get()) {
                    LivingEntity nextEntity = ServerUtils.getNearestEntity(nextEntities, originEntity);
                    if (nextEntity.isAlive()) {
                        ModPacketHandler.messageNearbyPlayers(
                                new SpawnElectricPathPacket(
                                        e.getX(),e.getY(),e.getZ(),
                                        nextEntity.getX(),nextEntity.getY(),nextEntity.getZ()
                                ),
                                (ServerLevel) e.getLevel(),
                                new Vec3(e.getX(),e.getY(),e.getZ()),
                                64
                        );
                    }
                }
            }


        //Wait before continuing

        //Make final versions of variables so they can be used in submit()
        final LivingEntity entityHit = e;
        final int newIteration = iteration+1;
        Util.backgroundExecutor().submit( () -> {
            try {
                Thread.sleep(CommonConfigs.ELECTRIC_JUMPTIME.get());
            } catch (Exception ignored) {
            }
            damage(entityHit, entitiesHit, newIteration, event, instrumentItems);
        });
    }

    //TODO: enchantment durability --- enchantment only has a limited amount of uses, and it removes itself when it's done

}
