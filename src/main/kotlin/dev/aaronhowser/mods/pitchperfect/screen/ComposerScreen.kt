package dev.aaronhowser.mods.pitchperfect.screen

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class ComposerScreen(
    val composerBlockEntity: ComposerBlockEntity,
    pTitle: Component
) : Screen(pTitle) {

    override fun renderBackground(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        this.renderTransparentBackground(pGuiGraphics)
        pGuiGraphics.blit(
            ScreenTextures.Backgrounds.COMPOSER,
            0,
            0,
            0,
            0,
            256,
            256
        )
    }

}