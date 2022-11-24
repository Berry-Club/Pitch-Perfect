package com.aaronhowser1.pitchperfect.item;

import com.aaronhowser1.pitchperfect.config.ClientConfigs;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.enchantment.BwaaapEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.HealingBeatEnchantment;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.ParticleTypes;
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

    public SoundEvent getSound() {
        return sound;
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
        spawnNote(level, pitch,
                player.getX()+noteVector.x(),
                player.getEyeY()+noteVector.y(),
                player.getZ()+noteVector.z()
        );

        //Enchantments
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.HEALING_BEAT.get(), itemStack) != 0) {
            HealingBeatEnchantment.heal(player);
            final float newPitch = pitch;
            HealingBeatEnchantment.getTargets(player).forEach(target -> {
                spawnNote(level, newPitch, target.getX(), target.getEyeY(), target.getZ());
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

    public void  attack(Entity target) {
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

                double noteX = target.getX() + entityWidth * (Math.random() * 3 - 1.5);
                double noteZ = target.getZ() + entityWidth * (Math.random() * 3 - 1.5);
                double noteY = target.getY() + entityHeight + (entityHeight * Math.random() * 1.5 - .75);
                spawnNote(
                        target.getLevel(),
                        randomPitch,
                        noteX, noteY, noteZ
                );
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

    public void spawnNote(Level level, float pitch, double x, double y, double z) {
        float noteColor = map(pitch, 2, 0.5F, 0, 0.5F) + 0.75F;
        level.addParticle(
                ParticleTypes.NOTE,
                x, y, z,
                getColor(noteColor, "red"),
                getColor(noteColor, "green"),
                getColor(noteColor, "blue")
        );
        if (false) //set true to debug
            System.out.println("Spawning particle:\nPosition: "+
                    x+","+y+","+z+
                    "\nPitch: "+String.valueOf(pitch)+
                    "\nColor: "+
                    String.valueOf(getColor(noteColor, "red"))+","+
                    String.valueOf(getColor(noteColor, "green"))+","+
                    String.valueOf(getColor(noteColor, "blue"))
            );
    }
    private float getColor(float pitch, String color) {
        return switch (color) {
            case "red" ->
                    Math.max(0.0F, Mth.sin((pitch + 0.0F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
            case "blue" ->
                    Math.max(0.0F, Mth.sin((pitch + 0.6666667F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
            case "green" ->
                    Math.max(0.0F, Mth.sin((pitch + 0.33333334F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
            default -> 0;
        };
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
