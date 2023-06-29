package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.bindVBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.render
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setLightMapCoords
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setMaterial
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setTexture
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.useModel
import dev.siro256.rtmpack.siromodels.block.platformdoor.WallTileEntity
import dev.siro256.rtmpack.siromodels.model.platformdoor.WallModel
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.renderer.base.CustomMachinePartsRenderer
import dev.siro256.rtmpack.siromodels.renderer.base.ModelViewMatrix
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Vector2f

class WallRenderer : CustomMachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as WallModel }

    override fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        modelViewMatrix: ModelViewMatrix,
        projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    ) {
        if (pass != RenderPass.NORMAL) return
        if (tileEntity !is WallTileEntity?) return

        TexturedShader
            .updateProjection(projectionMatrix)
            .setMaterial(currentMatId)
            .setTexture(currentTexture)
            .bindVBO(model.vbo)
            .setLightMapCoords(lightMapCoords)
            .setModelView(modelMatrix, modelViewMatrix)
            .useModel(model.base)
            .render()
            .useModel(model.body)
            .render()
    }
}
