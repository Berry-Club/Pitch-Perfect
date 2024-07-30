package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

class ToggleButton(
    width: Int,
    height: Int,
    message: Component,
    spriteWidth: Int,
    spriteHeight: Int,
    sprite: ResourceLocation,
    onPress: OnPress,
    narration: CreateNarration?
) : SpriteIconButton.CenteredIcon(
    width, height,
    message,
    spriteWidth, spriteHeight,
    sprite,
    onPress,
    narration
) {

    companion object {
        val SPRITE_LIGHT: ResourceLocation = ResourceLocation.withDefaultNamespace("widget/button")
        val SPRITE_DARK: ResourceLocation = ResourceLocation.withDefaultNamespace("widget/button_disabled")
    }

    var toggledOn = false

    override fun onPress() {
        this.toggledOn = !this.toggledOn
        super.onPress()
    }

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        val minecraft = Minecraft.getInstance()
        pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, this.alpha)
        RenderSystem.enableBlend()
        RenderSystem.enableDepthTest()

        pGuiGraphics.blitSprite(
            if (this.toggledOn) {
                SPRITE_DARK
            } else {
                SPRITE_LIGHT
            },
            this.x,
            this.y,
            this.getWidth(),
            this.getHeight()
        )

        if (this.isHovered) {
            pGuiGraphics.renderOutline(
                this.x,
                this.y,
                this.width,
                this.height,
                0xFFFFFFFF.toInt()
            )
        }

        pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        this.renderString(
            pGuiGraphics,
            minecraft.font,
            fgColor or (Mth.ceil(this.alpha * 255.0f) shl 24)
        )

        val i = this.x + this.getWidth() / 2 - this.spriteWidth / 2
        val j = this.y + this.getHeight() / 2 - this.spriteHeight / 2
        pGuiGraphics.blitSprite(this.sprite, i, j, this.spriteWidth, this.spriteHeight)
    }

}