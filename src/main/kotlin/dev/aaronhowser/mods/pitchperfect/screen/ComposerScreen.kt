package dev.aaronhowser.mods.pitchperfect.screen

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.ComposerControls
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.InstrumentArea
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.ScreenInstrument
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline.Timeline
import dev.aaronhowser.mods.pitchperfect.song.parts.SongWip
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
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

    lateinit var timeline: Timeline
        private set
    private lateinit var instrumentArea: InstrumentArea
    lateinit var composerControls: ComposerControls
        private set
    var leftPos: Int by Delegates.notNull()
    var topPos: Int by Delegates.notNull()

    override fun init() {
        leftPos = (width - ScreenTextures.Background.COMPOSER_WIDTH) / 2
        topPos = (height - ScreenTextures.Background.COMPOSER_HEIGHT) / 2

        timeline = Timeline(this, this.font)
        instrumentArea = InstrumentArea(this)
        composerControls = ComposerControls(this, this.font)

        timeline.init()
        instrumentArea.init()
        composerControls.init()
    }

    // Rendering

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

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

//        timeline.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        pGuiGraphics.drawString(
            font,
            selectedInstrument?.noteBlockInstrument?.name ?: "",
            leftPos + 5,
            topPos + 5 + 20,
            0xFFFFFF
        )
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
        timeline.timelineStepper.stopPlaying()
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

}