package dev.siro256.rtmpack.siromodels.renderer.base

import dev.siro256.rtmpack.siromodels.CustomModelObject
import dev.siro256.rtmpack.siromodels.deepCopy
import jp.ngt.rtm.block.tileentity.TileEntityOrnament
import jp.ngt.rtm.render.ModelObject
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

abstract class OrnamentRenderer<T : TileEntityOrnament> : TileEntitySpecialRenderer<T>(), Renderer {
    final override var currentTexture = -1

    final override fun render(
        tileEntity: T,
        x: Double, y: Double, z: Double,
        tickProgression: Float,
        destroyStage: Int,
        alpha: Float
    ) {
        if (currentTexture == -1) {
            tileEntity.resourceState?.resourceSet?.modelObj =
                tileEntity.resourceState?.resourceSet?.deepCopy(ModelObject::class.java, CustomModelObject())
            currentTexture = 0
        }

        val modelMatrix = Matrix4f().translate(x.toFloat(), y.toFloat(), z.toFloat())
        tileEntity.resourceState?.resourceSet?.config?.offset?.let { modelMatrix.translate(Vector3f(it)) }

        render(tileEntity, tickProgression, modelMatrix, tileEntity.getLightMapCoordinate())
    }

    abstract fun render(
        tileEntity: T?,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    )
}
