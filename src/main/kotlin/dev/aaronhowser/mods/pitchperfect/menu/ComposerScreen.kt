package dev.aaronhowser.mods.pitchperfect.menu

import com.mojang.blaze3d.systems.RenderSystem
import dev.aaronhowser.mods.pitchperfect.menu.base.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ComposerScreen(
    pMenu: ComposerMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<ComposerMenu>(pMenu, pPlayerInventory, pTitle) {

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, ScreenTextures.Backgrounds.COMPOSER)

        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        pGuiGraphics.blit(
            ScreenTextures.Backgrounds.COMPOSER,
            x,
            y,
            0,
            0,
            ScreenTextures.Backgrounds.TEXTURE_SIZE,
            ScreenTextures.Backgrounds.TEXTURE_SIZE
        )
    }

}