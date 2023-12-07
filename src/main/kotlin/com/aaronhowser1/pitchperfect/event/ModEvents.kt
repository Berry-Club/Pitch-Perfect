package com.aaronhowser1.pitchperfect.event

import com.aaronhowser1.pitchperfect.PitchPerfect
import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.song.SongRegistry
import com.aaronhowser1.pitchperfect.utils.CommonUtils.isMonster
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.NoteBlock
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.living.LivingSpawnEvent
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import kotlin.random.Random

@Suppress("unused")
@Mod.EventBusSubscriber(modid = PitchPerfect.MOD_ID)
object ModEvents {

    @SubscribeEvent
    fun onLivingHurt(event: LivingHurtEvent) {
        val target = event.entity
        val attacker: LivingEntity = event.source.entity as? LivingEntity ?: return

        AndHisMusicWasElectricEnchantment.handleElectric(event)
        attacker.mainHandItem.item.apply {
            if (this is InstrumentItem) this.attack(target)
        }

    }

    @SubscribeEvent
    fun onBreakBlock(event: BlockEvent.BreakEvent) {

        val level = event.level
        if (level !is ServerLevel) return

        val state = event.state

        try {

            if (state.block is NoteBlock) {
                val pitch = state.getValue(NoteBlock.NOTE)
                println(pitch)
            }
        } catch (e: Exception) {
            println(e.message)
        }

        SongRegistry.megalovania.toggle(level, event.player)

        event.isCanceled = true
    }

    @SubscribeEvent
    fun onLivingSpawn(event: LivingSpawnEvent) {
        val entity = event.entity
        if (
            !entity.isMonster() ||
            !entity.mainHandItem.isEmpty ||
            ServerConfig.MOB_SPAWNS_WITH_INSTRUMENT_CHANCE.get() < Random.nextFloat()
        ) return

        val randomInstrument = ModItems.ITEM_REGISTRY.entries.random().get()
        entity.setItemInHand(InteractionHand.MAIN_HAND, randomInstrument.defaultInstance)
    }

    @SubscribeEvent
    fun serverTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            ModScheduler.tick()
        }
    }
}
