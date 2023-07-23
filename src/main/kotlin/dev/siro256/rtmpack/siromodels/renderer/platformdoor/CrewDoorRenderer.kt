package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setModelMatrix
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.useModel
import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.gl.GLStateImpl
import dev.siro256.rtmpack.siromodels.block.platformdoor.CrewDoorTileEntity
import dev.siro256.rtmpack.siromodels.model.platformdoor.CrewDoorModel
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.renderer.base.CustomMachinePartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Vector2f

class CrewDoorRenderer : CustomMachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as CrewDoorModel }

    override fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    ) {
        if (pass != RenderPass.NORMAL) return
        if (tileEntity !is CrewDoorTileEntity?) return

        TexturedShader
            .setViewAndProjectionMatrix(GLStateImpl.getView(), GLStateImpl.getProjection())
            .setMaterial(currentMatId)
            .setTexture(currentTexture)
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
}
