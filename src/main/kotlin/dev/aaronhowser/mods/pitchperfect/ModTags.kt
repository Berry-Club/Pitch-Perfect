package dev.aaronhowser.mods.pitchperfect

import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType

object ModTags {

    private fun create(name: String): TagKey<EntityType<*>> =
        TagKey.create(Registries.ENTITY_TYPE, OtherUtil.modResource(name))

    val HEALING_BEAT_WHITELIST: TagKey<EntityType<*>> = create("healing_beat_whitelist")
    val HEALING_BEAT_BLACKLIST: TagKey<EntityType<*>> = create("healing_beat_blacklist")

}