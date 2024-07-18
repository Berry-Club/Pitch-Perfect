package dev.aaronhowser.mods.pitchperfect.screen

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.InstrumentArea
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.ScreenInstrument
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline.Timeline
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.properties.Delegates

@OnlyIn(Dist.CLIENT)
class ComposerScreen(
    val composerBlockEntity: ComposerBlockEntity,
    pTitle: Component
) : Screen(pTitle) {

    var selectedInstrument: ScreenInstrument? = null
    private val timeline by lazy { Timeline(this, this.font) }
    private val instrumentArea by lazy { InstrumentArea(this) }

    var leftPos: Int by Delegates.notNull()
    var topPos: Int by Delegates.notNull()

    override fun renderBackground(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        this.renderTransparentBackground(pGuiGraphics)
        pGuiGraphics.blit(
            ScreenTextures.Background.COMPOSER,
            leftPos, topPos,
            0f, 0f,              // Texture offset
            512, 256,             // Texture size
            512, 512    // Texture sheet size
        )
    }

    override fun init() {
        leftPos = (width - ScreenTextures.Background.COMPOSER_WIDTH) / 2
        topPos = (height - ScreenTextures.Background.COMPOSER_HEIGHT) / 2

        timeline.init()
        instrumentArea.init()
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        instrumentArea.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        timeline.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    override fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int): Boolean {
        instrumentArea.mouseClicked(pMouseX, pMouseY, pButton)
        timeline.mouseClicked(pMouseX, pMouseY, pButton)

        return super.mouseClicked(pMouseX, pMouseY, pButton)
    }

    override fun mouseScrolled(pMouseX: Double, pMouseY: Double, pScrollX: Double, pScrollY: Double): Boolean {
        if (pScrollY > 0) {
            timeline.verticalScrollIndex--
        } else if (pScrollY < 0) {
            timeline.verticalScrollIndex++
        }

        if (pScrollX > 0) {
            timeline.horizontalScrollIndex--
        } else if (pScrollX < 0) {
            timeline.horizontalScrollIndex++
        }

        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY)
    }

}