package dev.siro256.rtmpack.siromodels.renderer.base

import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.gl.GLStateImpl
import jp.ngt.rtm.modelpack.IResourceSelector
import jp.ngt.rtm.modelpack.modelset.ModelSetBase
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

abstract class TileEntityRenderer<T> : Renderer
        where T : TileEntity,
              T : IResourceSelector<*> {
    fun render(tileEntity: T?, modelSet: ModelSetBase<*>, x: Float, y: Float, z: Float, tickProgression: Float) {
        val modelName = modelSet.config.name
        val modelMatrix = Matrix4f().mul(modelOffset(tileEntity, modelName)).translate(x, y, z)
        modelSet.config.offset.let { modelMatrix.translate(Vector3f(it)) }
        val viewMatrix = if (tileEntity != null) GLStateImpl.getView() else GLStateImpl.getModelViewMatrixFromGL()
        val projectionMatrix =
            if (tileEntity != null) GLStateImpl.getProjection() else GLStateImpl.getProjectionMatrixFromGL()
        val lightMapCoords =
            if (tileEntity == null) {
                Vector2f(248.0F / 256.0F, 248.0F / 256.0F)
            } else {
                Vector2f((OpenGlHelper.lastBrightnessX + 8.0F) / 256.0F, (OpenGlHelper.lastBrightnessY + 8.0F) / 256.0F)
            }

        render(tileEntity, modelName, tickProgression, modelMatrix, viewMatrix, projectionMatrix, lightMapCoords)
    }

    open fun modelOffset(tileEntity: T?, modelName: String) = Matrix4f()

    protected abstract fun render(
        tileEntity: T?,
        modelName: String,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        viewMatrix: Matrix4f,
        projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    )
}
