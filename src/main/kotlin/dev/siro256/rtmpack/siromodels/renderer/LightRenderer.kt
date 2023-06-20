package dev.siro256.rtmpack.siromodels.renderer

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setColor
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setModelView
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.useModel
import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import dev.siro256.rtmpack.siromodels.renderer.base.CustomOrnamentPartsRenderer
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
        modelViewMatrix: Matrix4f,
        projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    ) {
        if (pass != RenderPass.NORMAL) return
        if (tileEntity !is TileEntityFluorescent?) return

        if (tileEntity != null) {
            when (tileEntity.dir.toInt()) {
                0 -> {
                    modelViewMatrix.translate(0.0F, 0.5F - MODEL_HEIGHT, 0.0F)
                }

                1 -> {
                    modelViewMatrix.translate(0.0F, 0.0F, 0.5F - MODEL_HEIGHT)
                    modelViewMatrix.rotateX(90.0F.toRadians())
                }

                2 -> {
                    modelViewMatrix.translate(0.0F, -0.5F + MODEL_HEIGHT, 0.0F)
                    modelViewMatrix.rotateX(180.0F.toRadians())
                }

                3 -> {
                    modelViewMatrix.translate(0.0F, 0.0F, -0.5F + MODEL_HEIGHT)
                    modelViewMatrix.rotateX((-90.0F).toRadians())
                }

                4 -> {
                    modelViewMatrix.translate(0.0F, 0.5F - MODEL_HEIGHT, 0.0F)
                    modelViewMatrix.rotateY(90.0F.toRadians())
                }

                5 -> {
                    modelViewMatrix.translate(0.5F - MODEL_HEIGHT, 0.0F, 0.0F)
                    modelViewMatrix.rotateY((-90.0F).toRadians())
                    modelViewMatrix.rotateX((-90.0F).toRadians())
                }

                6 -> {
                    modelViewMatrix.translate(0.0F, -0.5F + MODEL_HEIGHT, 0.0F)
                    modelViewMatrix.rotateY(90.0F.toRadians())
                    modelViewMatrix.rotateX((-180.0F).toRadians())
                }

                7 -> {
                    modelViewMatrix.translate(-0.5F + MODEL_HEIGHT, 0.0F, 0.0F)
                    modelViewMatrix.rotateY(90.0F.toRadians())
                    modelViewMatrix.rotateX((-90.0F).toRadians())
                }
            }
        }

        TexturedWithColorShader
            .updateProjection(projectionMatrix)
            .setMaterial(currentMatId)
            .setTexture(currentTexture)
            .bindVBO(model.vbo)
            .setModelView(modelViewMatrix)
            .setColor(0xffffffffu)
            .useModel(model.body)
            .render()
            .useModel(model.light)
            .render()
    }

    companion object {
        private const val MODEL_HEIGHT = 0.15F
    }
}
