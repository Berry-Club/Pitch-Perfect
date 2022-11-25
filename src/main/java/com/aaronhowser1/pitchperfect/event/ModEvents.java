package com.aaronhowser1.pitchperfect.event;

import com.aaronhowser1.pitchperfect.PitchPerfect;
import com.aaronhowser1.pitchperfect.client.ClientUtils;
import com.aaronhowser1.pitchperfect.config.ClientConfigs;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import com.aaronhowser1.pitchperfect.packets.ElectricParticleSpawnPacket;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.aaronhowser1.pitchperfect.packets.NoteParticleSpawnPacket;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = PitchPerfect.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof LivingEntity livingEntity) {

            //If attacking with an instrument
            if (livingEntity.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
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
                if (!livingEntity.getLevel().isClientSide()) {
                    List<Entity> nearbyEntities = livingEntity.getLevel().getEntities(
                            attacker,
                            new AABB(
                                    target.getX() - CommonConfigs.ELECTRIC_RANGE.get(),
                                    target.getY() - CommonConfigs.ELECTRIC_RANGE.get(),
                                    target.getZ() - CommonConfigs.ELECTRIC_RANGE.get(),
                                    target.getX() + CommonConfigs.ELECTRIC_RANGE.get(),
                                    target.getY() + CommonConfigs.ELECTRIC_RANGE.get(),
                                    target.getZ() + CommonConfigs.ELECTRIC_RANGE.get()
                            ),
                            (e) -> (e instanceof LivingEntity
                                    && e != target
                            )
                    );
                    for (Entity e : nearbyEntities) {

                        //Spawn particles
                        int particleAmountLowerBound = 1;
                        int particleAmountUpperBound = 2;
                        if (particleAmountLowerBound < particleAmountUpperBound) {
                            int randomAmount = (int) (Math.random() * (particleAmountUpperBound - particleAmountLowerBound) + particleAmountLowerBound);
                            double entityWidth = e.getBbWidth();
                            double entityHeight = e.getBbHeight();
                            for (int p = 1; p <= randomAmount; p++) {
                                float X = (float) (e.getX() + entityWidth * (Math.random() * .5 - .25));
                                float Z = (float) (e.getZ() + entityWidth * (Math.random() * .5 - .25));
                                float Y = (float) (e.getY() + entityHeight + (entityHeight * Math.random() * .5 - .25));
                                ModPacketHandler.messageNearbyPlayers(
                                        new ElectricParticleSpawnPacket(X, Y, Z),
                                        (ServerLevel) target.getLevel(),
                                        new Vec3(X, Y, Z),
                                        16
                                );
                            }
                        }

                        //Damage
                        e.hurt(DamageSource.LIGHTNING_BOLT, CommonConfigs.ELECTRIC_DAMAGE.get());
                    }
                }
            }
        }
    }
}
