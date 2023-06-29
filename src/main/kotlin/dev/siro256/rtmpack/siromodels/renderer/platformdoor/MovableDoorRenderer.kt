package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import com.github.kotatsu_rtm.kotatsulib.api.gl.VBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.useModel
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setColor
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.useModel
import dev.siro256.rtmpack.siromodels.block.platformdoor.MovableDoorTileEntity
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.model.platformdoor.DoorModel
import dev.siro256.rtmpack.siromodels.renderer.base.CustomMachinePartsRenderer
import dev.siro256.rtmpack.siromodels.renderer.base.ViewMatrix
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Matrix4fStack
import org.joml.Vector2f

class MovableDoorRenderer : CustomMachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as DoorModel }
    private var nanoTime = 0L

    override fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        viewMatrix: ViewMatrix,
        projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    ) {
        if (pass != RenderPass.NORMAL) return
        if (tileEntity !is MovableDoorTileEntity?) return

        nanoTime = System.nanoTime()

        val modelStack = Matrix4fStack(4).apply { set(modelMatrix) }
        val texturedShader =
            TexturedShader
                .updateProjection(projectionMatrix)
                .setMaterial(currentMatId)
                .setTexture(currentTexture)
                .bindVBO(model.vbo)
                .setLightMapCoords(lightMapCoords)
        val texturedWithColorShader =
            TexturedWithColorShader
                .updateProjection(projectionMatrix)
                .setMaterial(currentMatId)
                .setTexture(currentTexture)
                .bindVBO(model.vbo)
                .setLightMapCoords(lightMapCoords)

        texturedShader
            .setModelView(modelMatrix, viewMatrix)
            .useModel(model.base)
            .render()
            .useModel(model.body)
            .render()
            .useModel(model.controlPanel)
            .render()
            .useModel(model.maintenancePanelLeft)
            .render()
            .useModel(model.maintenancePanelRight)
            .render()

        drawDoor(tileEntity, modelStack, viewMatrix, texturedShader)
        drawDirection(tileEntity, modelStack, viewMatrix, texturedShader, texturedWithColorShader)
        drawNearIndicator(tileEntity, modelStack, viewMatrix, texturedShader)
    }

    private fun drawDoor(
        tileEntity: MovableDoorTileEntity?,
        modelMatrix: Matrix4fStack,
        viewMatrix: ViewMatrix,
        texturedShader: TexturedShader.Builder<Matrix4f, Int, Int, VBO.VertexNormalUV, Vector2f, Nothing, Nothing, Nothing>,
    ) {
        val leftMovement = if (tileEntity == null) -1.5F else -1.5F * tileEntity.doorOpeningLeft
        val rightMovement = if (tileEntity == null) 1.5F else 1.5F * tileEntity.doorOpeningRight

        modelMatrix.stack {
            modelMatrix.translate(leftMovement, 0.0F, 0.0F)
            texturedShader
                .setModelView(modelMatrix, viewMatrix)
                .useModel(model.doorLeft)
                .render()
        }

        modelMatrix.stack {
            modelMatrix.translate(rightMovement, 0.0F, 0.0F)
            texturedShader
                .setModelView(modelMatrix, viewMatrix)
                .useModel(model.doorRight)
                .render()
        }
    }

    private fun drawDirection(
        tileEntity: MovableDoorTileEntity?,
        modelMatrix: Matrix4fStack,
        viewMatrix: ViewMatrix,
        texturedShader: TexturedShader.Builder<Matrix4f, Int, Int, VBO.VertexNormalUV, Vector2f, Nothing, Nothing, Nothing>,
        texturedWithColorShader: TexturedWithColorShader.Builder<Matrix4f, Int, Int, VBO.VertexNormalUV, Vector2f, Nothing, Nothing, Nothing, Nothing>,
    ) {
        if (tileEntity is MovableDoorTileEntity && tileEntity.detectError) {
            val selector = nanoTime / 200_000_000 % 2 == 0L

            texturedShader
                .setModelView(modelMatrix, viewMatrix)
                .useModel(model.lamp1)
                .render()
                .useModel(model.lamp3)
                .render()
                .useModel(model.lamp4)
                .render()
                .useModel(model.lamp6)
                .render()
                .useModel(if (selector) model.lamp2 else model.lamp5)
                .render()

            texturedWithColorShader
                .setModelView(modelMatrix, viewMatrix)
                .setColor(0xe3172bffu)
                .useModel(if (!selector) model.lamp2 else model.lamp5)
                .render(disableLighting = true)

            return
        }

        var position = (nanoTime / 200_000_000 % 6).toInt()
        val direction = Direction.values()[(nanoTime / 5_000_000_000 % 3).toInt()]

        val textured = texturedShader.setModelView(modelMatrix, viewMatrix)
        val texturedWithColor =
            texturedWithColorShader.setModelView(modelMatrix, viewMatrix).setColor(0xff8c00ffu)

        for (i in 0 until 3) {
            textured.useModel(model.lamps[if (direction == Direction.RIGHT) 5 - position else position]).render()
            if (position == 5) position = 0 else ++position
        }

        for (i in 0 until 3) {
            if (direction == Direction.OFF) {
                textured.useModel(model.lamps[position]).render()
            } else {
                texturedWithColor
                    .useModel(model.lamps[if (direction == Direction.RIGHT) 5 - position else position])
                    .render(disableLighting = true)
            }
            if (position == 5) position = 0 else ++position
        }
    }

    private fun drawNearIndicator(
        tileEntity: MovableDoorTileEntity?,
        modelMatrix: Matrix4fStack,
        viewMatrix: ViewMatrix,
        texturedShader: TexturedShader.Builder<Matrix4f, Int, Int, VBO.VertexNormalUV, Vector2f, Nothing, Nothing, Nothing>,
    ) {
        val isLighting = nanoTime / 1_000_000_000 % 2 == 0L

        texturedShader
            .setModelView(modelMatrix, viewMatrix)
            .useModel(model.nearIndicator)
            .render(disableLighting = isLighting && (tileEntity != null && !tileEntity.detectError))
    }

    private enum class Direction {
        OFF,
        LEFT,
        RIGHT
    }
}
