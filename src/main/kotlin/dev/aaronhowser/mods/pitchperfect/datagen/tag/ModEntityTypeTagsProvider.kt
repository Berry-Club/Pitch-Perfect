package dev.aaronhowser.mods.pitchperfect.datagen.tag

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
    pOutput: PackOutput,
    pProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(pOutput, pProvider, PitchPerfect.ID, existingFileHelper) {

    companion object {
        private fun create(name: String): TagKey<EntityType<*>> =
            TagKey.create(Registries.ENTITY_TYPE, OtherUtil.modResource(name))

        val HEALING_BEAT_WHITELIST: TagKey<EntityType<*>> = create("healing_beat_whitelist")
        val HEALING_BEAT_BLACKLIST: TagKey<EntityType<*>> = create("healing_beat_blacklist")
    }

    override fun addTags(pProvider: HolderLookup.Provider) {
        this.tag(HEALING_BEAT_WHITELIST)
            .add(EntityType.SNOW_GOLEM)

        this.tag(HEALING_BEAT_BLACKLIST)
            .add(EntityType.ARMOR_STAND)
    }

}