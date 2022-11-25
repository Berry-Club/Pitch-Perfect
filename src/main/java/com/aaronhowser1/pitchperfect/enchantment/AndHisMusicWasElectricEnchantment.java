package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import com.aaronhowser1.pitchperfect.packets.ElectricParticleSpawnPacket;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class AndHisMusicWasElectricEnchantment extends Enchantment {
    protected AndHisMusicWasElectricEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    //iteration starts at 1
    public static void damage(Entity originEntity, List<Entity> entitiesHit, int iteration, LivingHurtEvent event,  InstrumentItem... instrumentItems) {

        List<Entity> entities = originEntity.getLevel().getEntities(
                originEntity,
                new AABB(
                        originEntity.getX() - CommonConfigs.ELECTRIC_RANGE.get(),
                        originEntity.getY() - CommonConfigs.ELECTRIC_RANGE.get(),
                        originEntity.getZ() - CommonConfigs.ELECTRIC_RANGE.get(),
                        originEntity.getX() + CommonConfigs.ELECTRIC_RANGE.get(),
                        originEntity.getY() + CommonConfigs.ELECTRIC_RANGE.get(),
                        originEntity.getZ() + CommonConfigs.ELECTRIC_RANGE.get()
                ),
                (e) -> (e instanceof LivingEntity
                        && !entitiesHit.contains(e)
                )
        );

        if (entities.isEmpty()) return;
        if (iteration > CommonConfigs.ELECTRIC_MAX_JUMPS.get()) return;
        Entity e = entities.get(0);

        for (Entity e2 : entities) {
            if (e2.distanceTo(originEntity) < e.distanceTo(originEntity)) {
                e = e2;
            }
        }

        if (e.isAlive()) {
            double entityWidth = e.getBbWidth();
            double entityHeight = e.getBbHeight();
            for (int p = 1; p <= Math.min(iteration,5); p++) {
                float X = (float) (e.getX() + entityWidth * (Math.random() * .75 - .375));
                float Z = (float) (e.getZ() + entityWidth * (Math.random() * .75 - .375));
                float Y = (float) (e.getY() + entityHeight + Math.min(2,(iteration*0.05)));
                ModPacketHandler.messageNearbyPlayers(
                        new ElectricParticleSpawnPacket(X, Y, Z),
                        (ServerLevel) e.getLevel(),
                        new Vec3(X, Y, Z),
                        16
                );
            }

            float damageFactor = CommonConfigs.ELECTRIC_DAMAGE_RETURNS.get() / iteration;

            //Damage
            e.hurt(
                    DamageSource.LIGHTNING_BOLT,
                    event.getAmount()*damageFactor
            );
        }

        entitiesHit.add(e);
        final Entity entityHit = e;

        final int newIteration = iteration+1;

        if (instrumentItems.length != 0) {
            instrumentItems[0].attack(e);
        }

        //Wait before continuing
        Util.backgroundExecutor().submit( () -> {
            try {
                Thread.sleep(CommonConfigs.ELECTRIC_JUMPTIME.get());
            } catch (Exception ignored) {
            }
                    damage(originEntity, entitiesHit, newIteration, event, instrumentItems);
        }
        );
    }
}
