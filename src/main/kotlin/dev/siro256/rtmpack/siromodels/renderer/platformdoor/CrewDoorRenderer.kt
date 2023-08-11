package dev.siro256.rtmpack.siromodels.renderer.platformdoor

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
import dev.siro256.rtmpack.siromodels.block.platformdoor.CrewDoorTileEntity
import dev.siro256.rtmpack.siromodels.deepCopy
import dev.siro256.rtmpack.siromodels.model.platformdoor.CrewDoorModel
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.renderer.base.MachineRenderer
import dev.siro256.rtmpack.siromodels.renderer.base.Renderer
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.RenderPass
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.joml.Matrix4f
import org.joml.Vector2f

object CrewDoorRenderer : MachineRenderer<CrewDoorTileEntity>() {
    private val textureLocation = ResourceLocation(Values.MOD_ID, "textures/machine/door.png")

    override fun render(
        tileEntity: CrewDoorTileEntity?,
        modelName: String,
        tickProgression: Float,
        modelMatrix: Matrix4f, viewMatrix: Matrix4f, projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f
    ) {
        val model = RenderDataManager.models[modelName] as? CrewDoorModel ?: return //TODO Render error model

        TexturedShader
            .setViewAndProjectionMatrix(viewMatrix, projectionMatrix)
            .setMaterial(0)
            .setTexture(getTextureId(textureLocation))
            .bindVBO(model.vbo)
            .setLightMapCoords(lightMapCoords)
            .setModelMatrix(modelMatrix)
            .useModel(model.base)
            .render()
            .useModel(model.post)
            .render()
            .useModel(model.door)
            .render()
            .useModel(model.hinge)
            .render()
            .useModel(model.rod1)
            .render()
            .useModel(model.rod2)
            .render()
            .useModel(model.rod3)
            .render()
            .useModel(model.lockBar)
            .render()
            .useModel(model.handle)
            .render()
    }

    object TileEntityRenderer : TileEntitySpecialRenderer<CrewDoorTileEntity>() {
        override fun render(
            tileEntity: CrewDoorTileEntity,
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

    class RTMRenderer : MachinePartsRenderer(), Renderer {
        private var modelObjectReplaced = false

        override fun render(tileEntity: TileEntity?, pass: RenderPass?, tickProgression: Float) {
            if (pass != RenderPass.NORMAL || tileEntity !is CrewDoorTileEntity?) return

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
