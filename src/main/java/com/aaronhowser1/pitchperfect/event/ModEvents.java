package com.aaronhowser1.pitchperfect.event;

import com.aaronhowser1.pitchperfect.PitchPerfect;
import com.aaronhowser1.pitchperfect.client.ClientUtils;
import com.aaronhowser1.pitchperfect.config.ClientConfigs;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import com.aaronhowser1.pitchperfect.packets.ElectricParticleSpawnPacket;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.aaronhowser1.pitchperfect.packets.NoteParticleSpawnPacket;
import net.minecraft.Util;
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

                    List<Entity> entitiesHit = new ArrayList<>();
                    entitiesHit.add(target);
                    entitiesHit.add(attacker);

                    if (attacker instanceof Player player && player.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
                        AndHisMusicWasElectricEnchantment.damage(target, entitiesHit, 1, event, instrumentItem);
                    } else {
                        AndHisMusicWasElectricEnchantment.damage(target, entitiesHit, 1, event);
                    }

                }
            }
        }
    }
}
