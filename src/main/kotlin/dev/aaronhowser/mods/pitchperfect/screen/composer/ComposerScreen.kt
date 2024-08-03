package dev.aaronhowser.mods.pitchperfect.screen.composer

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.ComposerControls
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.InstrumentArea
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.ScreenInstrument
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.timeline.Timeline
import dev.aaronhowser.mods.pitchperfect.song.parts.SongWip
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.properties.Delegates

@OnlyIn(Dist.CLIENT)
class ComposerScreen(
    val composerBlockEntity: ComposerBlockEntity,
    pTitle: Component = ModLanguageProvider.Block.COMPOSER.toComponent()
) : Screen(pTitle) {

    var selectedInstrument: ScreenInstrument? = null

    lateinit var timeline: Timeline
        private set
    private lateinit var instrumentArea: InstrumentArea
    lateinit var composerControls: ComposerControls
        private set
    var leftPos: Int by Delegates.notNull()
    var topPos: Int by Delegates.notNull()

    override fun init() {
        leftPos = (width - ScreenTextures.Background.Composer.WIDTH) / 2
        topPos = (height - ScreenTextures.Background.Composer.HEIGHT) / 2

        timeline = Timeline(this, this.font)
        instrumentArea = InstrumentArea(this, this.font)
        composerControls = ComposerControls(this, this.font)

        timeline.init()
        instrumentArea.init()
        composerControls.init()
    }

    // Rendering

    override fun renderBackground(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        this.renderTransparentBackground(pGuiGraphics)

        pGuiGraphics.blit(
            ScreenTextures.Background.Composer.COMPOSER,
            leftPos,
            topPos,
            0f,
            0f,
            ScreenTextures.Background.Composer.WIDTH,
            ScreenTextures.Background.Composer.HEIGHT,
            ScreenTextures.Background.Composer.CANVAS_SIZE,
            ScreenTextures.Background.Composer.CANVAS_SIZE
        )
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        pGuiGraphics.drawString(
            font,
            title,
            leftPos + 10,
            topPos + 10,
            0x403030,
            false
        )

        instrumentArea.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        for (widget in renderables) {
            if (widget !is Button) continue
            if (!widget.isHovered) continue

            val component = widget.message

            pGuiGraphics.renderComponentTooltip(
                font,
                listOf(component),
                pMouseX,
                pMouseY
            )

            break
        }

    }

    fun addRenderable(renderable: Renderable) {
        super.addRenderableOnly(renderable)
    }

    fun addRenderableWidgets(widgets: Collection<AbstractWidget>) {
        for (widget in widgets) {
            this.addRenderableWidget(widget)
        }
    }

    fun addRenderableWidgets(vararg widgets: AbstractWidget) {
        addRenderableWidgets(widgets.toList())
    }

    // Behavior

    override fun isPauseScreen(): Boolean {
        return false
    }

    var songWip: SongWip? = null
        private set

    override fun tick() {
        if (composerBlockEntity.isRemoved) {
            minecraft?.player?.closeContainer()
        }

        if (songWip != composerBlockEntity.songWip) {
            songWip = composerBlockEntity.songWip
            timeline.setLastBeatDelay()
        }
    }

    override fun onClose() {
        composerControls.stopPlaying()
        super.onClose()
    }

    // Controls

    override fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int): Boolean {
        timeline.mouseClicked(pMouseX, pMouseY, pButton)

        if (!composerControls.jumpToBeatBox.isHovered) {
            composerControls.jumpToBeatBox.isFocused = false
        } else {
            if (pButton == 1) {
                composerControls.jumpToBeatBox.value = ""
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton)
    }

    override fun mouseScrolled(pMouseX: Double, pMouseY: Double, pScrollX: Double, pScrollY: Double): Boolean {
        if (pScrollY > 0 || pScrollX > 0) {
            timeline.horizontalScrollIndex--
        } else if (pScrollY < 0 || pScrollX < 0) {
            timeline.horizontalScrollIndex++
        }

        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY)
    }

    override fun keyPressed(pKeyCode: Int, pScanCode: Int, pModifiers: Int): Boolean {

        if (isCopy(pKeyCode) || isCut(pKeyCode)) {
            composerControls.copySong()
        } else if (isPaste(pKeyCode)) {
            composerControls.pasteSong()
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers)
    }

}