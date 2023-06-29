package dev.siro256.rtmpack.siromodels.renderer

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setColor
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.useModel
import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import dev.siro256.rtmpack.siromodels.renderer.base.CustomOrnamentPartsRenderer
import dev.siro256.rtmpack.siromodels.renderer.base.ViewMatrix
import jp.ngt.rtm.block.tileentity.TileEntityFluorescent
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Vector2f

class LightRenderer : CustomOrnamentPartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as LightModel }

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
        if (tileEntity !is TileEntityFluorescent?) return

        if (tileEntity != null) {
            when (tileEntity.dir.toInt()) {
                0 -> {
                    modelMatrix.translate(0.0F, 0.5F - MODEL_HEIGHT, 0.0F)
                }

                1 -> {
                    modelMatrix.translate(0.0F, 0.0F, 0.5F - MODEL_HEIGHT)
                    modelMatrix.rotateX(90.0F.toRadians())
                }

                2 -> {
                    modelMatrix.translate(0.0F, -0.5F + MODEL_HEIGHT, 0.0F)
                    modelMatrix.rotateX(180.0F.toRadians())
                }

                3 -> {
                    modelMatrix.translate(0.0F, 0.0F, -0.5F + MODEL_HEIGHT)
                    modelMatrix.rotateX((-90.0F).toRadians())
                }

                4 -> {
                    modelMatrix.translate(0.0F, 0.5F - MODEL_HEIGHT, 0.0F)
                    modelMatrix.rotateY(90.0F.toRadians())
                }

                5 -> {
                    modelMatrix.translate(0.5F - MODEL_HEIGHT, 0.0F, 0.0F)
                    modelMatrix.rotateY((-90.0F).toRadians())
                    modelMatrix.rotateX((-90.0F).toRadians())
                }

                6 -> {
                    modelMatrix.translate(0.0F, -0.5F + MODEL_HEIGHT, 0.0F)
                    modelMatrix.rotateY(90.0F.toRadians())
                    modelMatrix.rotateX((-180.0F).toRadians())
                }

                7 -> {
                    modelMatrix.translate(-0.5F + MODEL_HEIGHT, 0.0F, 0.0F)
                    modelMatrix.rotateY(90.0F.toRadians())
                    modelMatrix.rotateX((-90.0F).toRadians())
                }
            }
        }

        TexturedWithColorShader
            .updateProjection(projectionMatrix)
            .setMaterial(currentMatId)
            .setTexture(currentTexture)
            .bindVBO(model.vbo)
            .setLightMapCoords(lightMapCoords)
            .setModelView(modelMatrix, viewMatrix)
            .setColor(0xffffffffu)
            .useModel(model.body)
            .render(disableLighting = true)
            .useModel(model.light)
            .render(disableLighting = true)
    }

    companion object {
        private const val MODEL_HEIGHT = 0.15F
    }
}
