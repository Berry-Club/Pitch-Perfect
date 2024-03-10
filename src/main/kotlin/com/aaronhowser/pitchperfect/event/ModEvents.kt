package com.aaronhowser.pitchperfect.event

import com.aaronhowser.pitchperfect.PitchPerfect
import com.aaronhowser.pitchperfect.config.ServerConfig
import com.aaronhowser.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import com.aaronhowser.pitchperfect.item.InstrumentItem
import com.aaronhowser.pitchperfect.item.ModItems
import com.aaronhowser.pitchperfect.utils.CommonUtils.isMonster
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.living.LivingSpawnEvent
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

//    @SubscribeEvent
//    fun onBreakBlock(event: BlockEvent.BreakEvent) {
//
//        val level = event.level
//        if (level !is ServerLevel) return
//
//        val state = event.state
//
//        try {
//
//            if (state.block is NoteBlock) {
//                val pitch = state.getValue(NoteBlock.NOTE)
//                println(pitch)
//            }
//        } catch (e: Exception) {
//            println(e.message)
//        }
//
//        SongRegistry.megalovania.toggle(level, event.player)
//
//        event.isCanceled = true
//    }


    //TODO:
    // Apparently this might also trigger on mob DESPAWN, which is whatever. But like. Who cares? The mob is despawning.
    // Maybe I could mixin to Zombie#populateDefaultEquipmentSlots
    @SubscribeEvent
    fun onLivingSpawn(event: LivingSpawnEvent) {
        if (event is LivingSpawnEvent.AllowDespawn) return

        val entity = event.entity

        if (!entity.isMonster()) return
        if (!entity.mainHandItem.isEmpty) return

        val chance = ServerConfig.MOB_SPAWNS_WITH_INSTRUMENT_CHANCE.get()
        val random = Random.nextDouble()

        if (chance < random) return

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
