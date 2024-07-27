package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Consumer

class ModAdvancementSubProvider : AdvancementProvider.AdvancementGenerator {

    private lateinit var registries: HolderLookup.Provider
    private lateinit var saver: Consumer<AdvancementHolder>
    private lateinit var existingFileHelper: ExistingFileHelper

    private fun guide(string: String) = OtherUtil.modResource("guide/$string")

    private fun AdvancementHolder.add(): AdvancementHolder {
        this@ModAdvancementSubProvider.saver.accept(this)
        return this
    }

    override fun generate(
        registries: HolderLookup.Provider,
        saver: Consumer<AdvancementHolder>,
        existingFileHelper: ExistingFileHelper
    ) {
        this.registries = registries
        this.saver = saver
        this.existingFileHelper = existingFileHelper

        val root = makeRoot()

        val hitMob =
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    Items.ZOMBIE_HEAD,
                    ModLanguageProvider.Advancement.HIT_MOB_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.HIT_MOB_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "hit_mob",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("hit_mob"))
                .add()

    }

    private fun makeRoot(): AdvancementHolder {
        val rootBuilder =
            Advancement.Builder.advancement()
                .display(
                    ModItems.BANJO.get(),
                    Component.literal("Pitch Perfect"),
                    ModLanguageProvider.Advancement.ROOT_DESC.toComponent(),
                    OtherUtil.modResource("textures/block/machine_bottom.png"),
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                )
                .requirements(AdvancementRequirements.Strategy.OR)

        for (instrumentItem in ModItems.instruments) {
            rootBuilder.addCriterion(
                "has_${instrumentItem.id.path}",
                InventoryChangeTrigger.TriggerInstance.hasItems(instrumentItem.get())
            )
        }

        return rootBuilder
            .build(guide("root"))
            .add()
    }
}