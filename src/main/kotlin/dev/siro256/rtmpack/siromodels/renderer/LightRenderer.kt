package dev.siro256.rtmpack.siromodels.renderer

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setModelMatrix
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.useModel
import dev.siro256.rtmpack.siromodels.CustomModelObject
import dev.siro256.rtmpack.siromodels.Values
import dev.siro256.rtmpack.siromodels.block.ornament.TileEntityLight
import dev.siro256.rtmpack.siromodels.deepCopy
import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import dev.siro256.rtmpack.siromodels.renderer.base.OrnamentRenderer
import dev.siro256.rtmpack.siromodels.renderer.base.Renderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.OrnamentPartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.joml.Matrix4f
import org.joml.Vector2f

object LightRenderer : OrnamentRenderer<TileEntityLight>() {
    private const val MODEL_HEIGHT = 0.15F
    private val textureLocation = ResourceLocation(Values.MOD_ID, "textures/ornament/light.png")

    override fun render(
        tileEntity: TileEntityLight?,
        modelName: String,
        tickProgression: Float,
        modelMatrix: Matrix4f, viewMatrix: Matrix4f, projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f
    ) {
        val model = RenderDataManager.models[modelName] as LightModel

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
            .setViewAndProjectionMatrix(viewMatrix, projectionMatrix)
            .setMaterial(0)
            .setTexture(getTextureId(textureLocation))
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
            render(
                tileEntity,
                tileEntity.resourceState.resourceSet,
                x.toFloat(), y.toFloat(), z.toFloat(),
                tickProgression
            )
        }
    }

    class RTMRenderer : OrnamentPartsRenderer(), Renderer {
        private var modelObjectReplaced = false

        override fun render(tileEntity: TileEntity?, pass: RenderPass?, tickProgression: Float) {
            if (pass != RenderPass.NORMAL || tileEntity !is TileEntityLight?) return

            if (!modelObjectReplaced) {
                modelObjectReplaced = true
                modelSet.modelObj = modelSet.modelObj.deepCopy(ModelObject::class.java, CustomModelObject())
            }

            render(
                tileEntity,
                modelSet,
                tileEntity?.x?.let { it.toFloat() - TileEntityRendererDispatcher.staticPlayerX.toFloat() } ?: 0.0F,
                tileEntity?.y?.let { it.toFloat() - TileEntityRendererDispatcher.staticPlayerY.toFloat() } ?: 0.0F,
                tileEntity?.z?.let { it.toFloat() - TileEntityRendererDispatcher.staticPlayerZ.toFloat() } ?: 0.0F,
                tickProgression
            )
        }
    }
}
