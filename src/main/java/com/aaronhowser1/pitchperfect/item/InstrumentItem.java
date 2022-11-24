package com.aaronhowser1.pitchperfect.item;

import com.aaronhowser1.pitchperfect.config.ClientConfigs;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.enchantment.BwaaapEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.HealingBeatEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.aaronhowser1.pitchperfect.packets.NoteParticleSpawnPacket;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;

import java.util.Map;

public class InstrumentItem extends Item {

    public final SoundEvent sound;
    private final Lazy<ImmutableSetMultimap<Attribute, AttributeModifier>> attributeModifiers;

    public InstrumentItem(SoundEvent s) {
        super(new Item.Properties()
                .stacksTo(1)
                .tab(ModCreativeModeTab.MOD_TAB)
        );
        this.sound = s;
        attributeModifiers = buildDefaultAttributes();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Vec3 lookVector = player.getLookAngle();
        float pitch = (float) lookVector.y();
        pitch = map(pitch, -1,1,0.5F,2);
        playSound(level, pitch, player.getX(), player.getY(), player.getZ(), ClientConfigs.VOLUME.get());

        Vec3 noteVector = lookVector;
        if (interactionHand.equals(InteractionHand.MAIN_HAND)) {
            noteVector = lookVector.yRot(-0.5F);
        } else {
            noteVector = lookVector.yRot(0.5F);
        }
        if (!level.isClientSide()) {
            ModPacketHandler.messageNearbyPlayers(
                    new NoteParticleSpawnPacket(sound.getLocation(),
                            pitch,
                            (float) (player.getX()+noteVector.x()),
                            (float) (player.getEyeY()+noteVector.y()),
                            (float) (player.getZ()+noteVector.z())
                    ),
                    (ServerLevel) player.getLevel(),
                    new Vec3(player.getX(), player.getY(), player.getZ()),
                    64
            );
        }

        //Enchantments
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.HEALING_BEAT.get(), itemStack) != 0) {
            HealingBeatEnchantment.heal(player);
            final float newPitch = pitch;
            HealingBeatEnchantment.getTargets(player).forEach(target -> {
                if (!level.isClientSide()) {
                    ModPacketHandler.messageNearbyPlayers(
                            new NoteParticleSpawnPacket(sound.getLocation(),
                                    newPitch,
                                    (float) (target.getX()),
                                    (float) (target.getEyeY()),
                                    (float) (target.getZ())
                            ),
                            (ServerLevel) target.getLevel(),
                            new Vec3(target.getX(), target.getEyeY(), target.getZ()),
                            64
                    );
                }
            });
            player.getCooldowns().addCooldown(this, (int) (HealingBeatEnchantment.getTargets(player).size() * CommonConfigs.HEAL_COOLDOWN_MULT.get()));
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BWAAAP.get(), itemStack) != 0) {
            BwaaapEnchantment.knockback(player);
            player.getCooldowns().addCooldown(this, (int) BwaaapEnchantment.getTargets(player).size() * CommonConfigs.BWAAAP_COOLDOWN_MULT.get());
        }

        return InteractionResultHolder.fail(itemStack);
    }

//    @Override
//    public boolean onLeftClickEntity(ItemStack stack, Player attacker, Entity target) {
//        attack(stack, attacker,target);
//        return super.onLeftClickEntity(stack, attacker, target);
//    }

    public void attack(Entity target) {
//        int particleAmountLowerBound = ClientConfigs.MIN_ATTACK_PARTICLES.get();
//        int particleAmountUpperBound = ClientConfigs.MAX_ATTACK_PARTICLES.get();
        int particleAmountLowerBound = 2;
        int particleAmountUpperBound = 4;

        if (particleAmountLowerBound < particleAmountUpperBound) {
            int randomAmount = (int) (Math.random() * (particleAmountUpperBound - particleAmountLowerBound) + particleAmountLowerBound);

            double entityWidth = target.getBbWidth();
            double entityHeight = target.getBbHeight();

            for (int note = 1; note <= randomAmount; note++) {
                float randomPitch = (int) (Math.random() * 180) - 90; //random number [0,180] -> [-90,90]
                randomPitch = map(randomPitch, -90, 90, 2, 0.5F); //from [-90,90] to [2,0.5], high->low bc big number = low pitch

                float noteX = (float) (target.getX() + entityWidth * (Math.random() * 3 - 1.5));
                float noteZ = (float) (target.getZ() + entityWidth * (Math.random() * 3 - 1.5));
                float noteY = (float) (target.getY() + entityHeight + (entityHeight * Math.random() * 1.5 - .75));

                ModPacketHandler.messageNearbyPlayers(
                    new NoteParticleSpawnPacket(sound.getLocation(), randomPitch, noteX, noteY, noteZ),
                        (ServerLevel) target.getLevel(),
                        new Vec3(noteX, noteY, noteZ),
                        16
                );

                //TODO: Find out why this isn't working
                playSound(target.getLevel(), randomPitch, noteX, noteY, noteZ, Math.max(ClientConfigs.VOLUME.get() / randomAmount, ClientConfigs.MIN_ATTACK_VOLUME.get()));
            }
        }
    }

    public float map(float value, float min1, float max1, float min2, float max2)
    {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1));
    }

    private void playSound(Level level, float pitch, double x, double y, double z, float volume) {
        level.playLocalSound(
                x,
                y,
                z,
                this.sound,
                SoundSource.PLAYERS,
                volume,
                pitch,
                false
        );
    }



    @Override
    public int getEnchantmentValue() {
        return 8;
    }

    //Ok beyond this, I barely understood
    //Mostly stolen from https://github.com/Tslat/Advent-Of-Ascension/blob/1.18.2/source/content/item/weapon/maul/BaseMaul.java (with permission, thanks Tslat)
    public float getAttackDamage() {
        return 0.01F;
    }

    //Lazy is apparently a map, of type ImmutableSetMultimap, which maps attributes to how they're being modified
    //Like, damage to +2, or whatever
    protected Lazy<ImmutableSetMultimap<Attribute, AttributeModifier>> buildDefaultAttributes() {
        return Lazy.of(() -> ImmutableSetMultimap.of(
                Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getAttackDamage(), AttributeModifier.Operation.ADDITION)
        ));
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        //If and only if item is in the hand, for some reason
        if (slot == EquipmentSlot.MAINHAND) {
            //Make a new map
            Multimap<Attribute, AttributeModifier> newMap = HashMultimap.create();
            //Get the existing one, made by the Lazy constructor
            ImmutableSetMultimap<Attribute, AttributeModifier> attributes = this.attributeModifiers.get();
            //GET EVERY VALUE OF THE ORIGINAL and put it in the new one
            for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
                newMap.put(entry.getKey(), entry.getValue());
            }
            //Return the new one
            return newMap;
            //This is so fucking stupid
        }

        return super.getAttributeModifiers(slot, stack);
    }
}
