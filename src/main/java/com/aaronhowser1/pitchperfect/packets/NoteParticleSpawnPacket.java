package com.aaronhowser1.pitchperfect.packets;

import com.aaronhowser1.pitchperfect.client.ClientUtils;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NoteParticleSpawnPacket implements ModPacket{

    private final ResourceLocation soundResourceLocation;
    private final float pitch;
    private final float x;
    private final float y;
    private final float z;


    public NoteParticleSpawnPacket(ResourceLocation soundResourceLocation, float pitch, float x, float y, float z) {
        this.soundResourceLocation = soundResourceLocation;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(soundResourceLocation);
        buffer.writeFloat(pitch);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
    }

    public static NoteParticleSpawnPacket decode(FriendlyByteBuf buffer) {
        return new NoteParticleSpawnPacket(
                buffer.readResourceLocation(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {

        ClientUtils.spawnNote(pitch, x, y, z);

        context.get().setPacketHandled(true);
    }

}
