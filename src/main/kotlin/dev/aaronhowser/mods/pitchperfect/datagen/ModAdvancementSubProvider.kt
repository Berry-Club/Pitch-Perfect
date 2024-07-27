package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
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

        val root =
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
                .addCriterion(
                    "any_instrument",
                    InventoryChangeTrigger.TriggerInstance.hasItems(*ModItems.instruments.toTypedArray())
                )
                .build(guide("root"))
                .add()

    }
}