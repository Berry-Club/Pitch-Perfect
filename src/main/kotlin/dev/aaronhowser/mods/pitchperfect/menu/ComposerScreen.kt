package dev.aaronhowser.mods.pitchperfect.menu

import dev.aaronhowser.mods.pitchperfect.menu.base.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ComposerScreen(
    pMenu: ComposerMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<ComposerMenu>(pMenu, pPlayerInventory, pTitle) {

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.blit(
            ScreenTextures.Backgrounds.COMPOSER,
            leftPos,
            topPos,
            0,
            0,
            ScreenTextures.Backgrounds.TEXTURE_SIZE,
            ScreenTextures.Backgrounds.TEXTURE_SIZE
        )
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pGuiGraphics, pMouseX, pMouseY)
    }

}