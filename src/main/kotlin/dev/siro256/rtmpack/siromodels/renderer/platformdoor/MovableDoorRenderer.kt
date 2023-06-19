package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import dev.siro256.rtmpack.siromodels.block.platformdoor.MovableDoorTileEntity
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.model.platformdoor.DoorModel
import jp.ngt.ngtlib.renderer.GLHelper
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11

class MovableDoorRenderer: MachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as DoorModel }
    private var nanoTime = 0L

    override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (pass != RenderPass.NORMAL) return

        if (tileEntity !is MovableDoorTileEntity?) return

        nanoTime = System.nanoTime()

        model.base.render(this)
        model.body.render(this)
        model.controlPanel.render(this)

        model.maintenancePanelLeft.render(this)
        model.maintenancePanelRight.render(this)

        drawDoor(tileEntity)
        drawDirection(tileEntity)
        drawNearIndicator(tileEntity)
    }

    private fun drawDoor(tileEntity: MovableDoorTileEntity?) {
        val leftMovement = if (tileEntity == null) -1.5F else -1.5F * tileEntity.doorOpeningLeft
        val rightMovement = if (tileEntity == null) 1.5F else 1.5F * tileEntity.doorOpeningRight

        GL11.glPushMatrix()
        GL11.glTranslatef(leftMovement, 0.0F, 0.0F)
        model.doorLeft.render(this)
        GL11.glPopMatrix()

        GL11.glPushMatrix()
        GL11.glTranslatef(rightMovement, 0.0F, 0.0F)
        model.doorRight.render(this)
        GL11.glPopMatrix()
    }

    private fun drawDirection(tileEntity: MovableDoorTileEntity?) {
        if (tileEntity is MovableDoorTileEntity && tileEntity.detectError) {
            GLHelper.setColor(0xc0c0c0, 255)

            model.directionLamp.lamp1.render(this)
            model.directionLamp.lamp3.render(this)
            model.directionLamp.lamp4.render(this)
            model.directionLamp.lamp6.render(this)

            val selector = nanoTime / 200_000_000 % 2 == 0L

            if (selector) {
                model.directionLamp.lamp2.render(this)
            } else {
                model.directionLamp.lamp5.render(this)
            }

            GLHelper.setColor(0xe3172b, 255)
            GLHelper.setBrightness(0xffff)

            if (!selector) {
                model.directionLamp.lamp2.render(this)
            } else {
                model.directionLamp.lamp5.render(this)
            }

            //reset
            GLHelper.setColor(0xffffff, 255)
            GLHelper.setBrightness(0x0)

            return
        }

        var position = (nanoTime / 200_000_000 % 6).toInt()
        val direction = Direction.values()[(nanoTime / 5_000_000_000 % 3).toInt()]
        GLHelper.setColor(0xc0c0c0, 255)

        for (i in 0 until 6) {
            if (i == 3 && direction != Direction.OFF) {
                GLHelper.setColor(0xff8c00, 255)
                GLHelper.setBrightness(0xffff)
            }

            val selector = if (direction == Direction.RIGHT) 5 - position else position
            model.directionLamp.list[selector].render(this)

            if (position == 5) position = 0 else position++
        }

        //reset
        GLHelper.setColor(0xffffff, 255)
        GLHelper.setBrightness(0x0)
    }

    private fun drawNearIndicator(tileEntity: MovableDoorTileEntity?) {
        val isLighting = nanoTime / 1_000_000_000 % 2 == 0L

        if (isLighting && (tileEntity != null && !tileEntity.detectError))
            GLHelper.setBrightness(0x9999)

        model.nearIndicator.render(this)

        GLHelper.setBrightness(0x0)
    }

    private enum class Direction {
        OFF,
        LEFT,
        RIGHT
    }
}
