package dev.aaronhowser.mods.pitchperfect.menu

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class ComposerScreen(
    val composerBlockEntity: ComposerBlockEntity,
    pTitle: Component
) : Screen(pTitle) {

}