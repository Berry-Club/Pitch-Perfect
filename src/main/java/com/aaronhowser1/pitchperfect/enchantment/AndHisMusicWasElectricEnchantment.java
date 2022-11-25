package com.aaronhowser1.pitchperfect.enchantment;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.packets.ElectricParticleSpawnPacket;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class AndHisMusicWasElectricEnchantment extends Enchantment {
    protected AndHisMusicWasElectricEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void damage(Entity originalHit, List<Entity> entities, int iteration, LivingHurtEvent event) {

        if (entities.isEmpty()) return;
        Entity e = entities.get(0);

        for (Entity e2 : entities) {
            if (e2.distanceTo(originalHit) < e.distanceTo(originalHit)) {
                e = e2;
            }
        }

        if (e.isAlive()) {
            double entityWidth = e.getBbWidth();
            double entityHeight = e.getBbHeight();
            for (int p = 1; p <= iteration; p++) {
                float X = (float) (e.getX() + entityWidth * (Math.random() * .75 - .375));
                float Z = (float) (e.getZ() + entityWidth * (Math.random() * .75 - .375));
                float Y = (float) (e.getY() + entityHeight + (iteration*0.05));
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

        entities.remove(e);
        iteration++;
        damage(originalHit, entities, iteration, event);
    }
}
