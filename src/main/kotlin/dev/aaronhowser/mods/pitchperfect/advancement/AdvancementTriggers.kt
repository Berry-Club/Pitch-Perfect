package dev.aaronhowser.mods.pitchperfect.advancement

import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.server.level.ServerPlayer

object AdvancementTriggers {

    private fun completeAdvancement(player: ServerPlayer, advancement: AdvancementHolder) {
        val progress = player.advancements.getOrStartProgress(advancement)
        if (progress.isDone) return

        val criteria = progress.remainingCriteria.iterator()

        while (criteria.hasNext()) {
            val criterion = criteria.next()
            player.advancements.award(advancement, criterion)
        }
    }

    fun hitWithInstrument(player: ServerPlayer) {
        val advancement =
            player.server.advancements.get(OtherUtil.modResource("guide/hit_mob")) ?: return

        completeAdvancement(player, advancement)
    }

}