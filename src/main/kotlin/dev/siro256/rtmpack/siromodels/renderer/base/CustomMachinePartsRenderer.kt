package dev.siro256.rtmpack.siromodels.renderer.base

import dev.siro256.rtmpack.siromodels.CustomModelObject
import dev.siro256.rtmpack.siromodels.deepCopy
import jp.ngt.rtm.block.tileentity.TileEntityMachineBase
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.RenderPass
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.tileentity.TileEntity
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

abstract class CustomMachinePartsRenderer : MachinePartsRenderer(), Renderer {
    final override var currentTexture = -1

    @Suppress("DuplicatedCode")
    final override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (tileEntity !is TileEntityMachineBase?)
            throw IllegalArgumentException("An unexpected type \"${tileEntity?.javaClass}\" was passed")

        if (currentTexture == -1) {
            modelSet.modelObj = modelSet.modelObj.deepCopy(ModelObject::class.java, CustomModelObject())
            currentTexture = 0
        }

        val modelMatrix = Matrix4f()
        tileEntity?.pos?.let { modelMatrix.translate(it.x.toFloat(), it.y.toFloat(), it.z.toFloat()) }
        tileEntity?.resourceState?.resourceSet?.config?.offset?.let { modelMatrix.translate(Vector3f(it)) }

        val lightMapCoords =
            if (tileEntity == null) {
                Vector2f(248.0F / 256.0F, 248.0F / 256.0F)
            } else {
                Vector2f((OpenGlHelper.lastBrightnessX + 8.0F) / 256.0F, (OpenGlHelper.lastBrightnessY + 8.0F) / 256.0F)
            }

        render(tileEntity, pass, tickProgression, modelMatrix, lightMapCoords)
    }

    abstract fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    )
}
