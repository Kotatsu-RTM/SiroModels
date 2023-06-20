package dev.siro256.rtmpack.siromodels.renderer

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setModelView
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.useModel
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

        TexturedShader
            .updateProjection(projectionMatrix)
            .setMaterial(currentMatId)
            .setTexture(currentTexture)
            .bindVBO(model.vbo)
            .setLightMapCoords(lightMapCoords)
            .setModelView(modelViewMatrix)
            .useModel(model.body)
            .render()
            .useModel(model.light)
            .render()
    }

    companion object {
        private const val MODEL_HEIGHT = 0.15F
    }
}
