package dev.siro256.rtmpack.siromodels.renderer

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setModelMatrix
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.useModel
import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.gl.GLStateImpl
import dev.siro256.rtmpack.siromodels.block.ornament.TileEntityLight
import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import dev.siro256.rtmpack.siromodels.renderer.base.CustomOrnamentPartsRenderer
import jp.ngt.rtm.block.tileentity.TileEntityFluorescent
import jp.ngt.rtm.render.RenderPass
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Vector2f

object LightRenderer : CustomOrnamentPartsRenderer() {
    private const val MODEL_HEIGHT = 0.15F

    private val model by lazy { RenderDataManager.models[modelName] as LightModel }

    override fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    ) {
        RuntimeException().printStackTrace()
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

        TexturedShader
            .setViewAndProjectionMatrix(GLStateImpl.getView(), GLStateImpl.getProjection())
            .setMaterial(currentMatId)
            .setTexture(currentTexture)
            .bindVBO(model.vbo)
            .setLightMapCoords(lightMapCoords)
            .setModelMatrix(modelMatrix)
            .useModel(model.all)
            .render(disableLighting = true)
    }

    object TileEntityRenderer : TileEntitySpecialRenderer<TileEntityLight>() {
        override fun render(
            tileEntity: TileEntityLight,
            x: Double, y: Double, z: Double,
            tickProgression: Float,
            destroyStage: Int,
            alpha: Float,
        ) {
            render(tileEntity, RenderPass.NORMAL, tickProgression)
        }
    }
}
