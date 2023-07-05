package dev.siro256.rtmpack.siromodels.renderer.base

import dev.siro256.rtmpack.siromodels.CustomModelObject
import dev.siro256.rtmpack.siromodels.deepCopy
import jp.ngt.rtm.block.tileentity.TileEntityMachineBase
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.RenderPass
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import kotlin.properties.Delegates

abstract class CustomMachinePartsRenderer : MachinePartsRenderer(), Renderer {
    private var previousFrameIndex = Int.MIN_VALUE
    private var previousTileEntityPos = BlockPos(100_000_000, 100_000_000, 100_000_000)

    private var viewMatrix by Delegates.notNull<Matrix4f>()
    private var projectionMatrix by Delegates.notNull<Matrix4f>()
    private val matrixBuffer = GLAllocation.createDirectFloatBuffer(16)

    final override var currentTexture = -1

    @Suppress("DuplicatedCode")
    final override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (tileEntity !is TileEntityMachineBase?)
            throw IllegalArgumentException("An unexpected type \"${tileEntity?.javaClass}\" was passed")

        if (currentTexture == -1) {
            modelSet.modelObj = modelSet.modelObj.deepCopy(ModelObject::class.java, CustomModelObject())
            currentTexture = 0
        }

        val frameIndex = Minecraft.getMinecraft().frameTimer.index

        if (previousFrameIndex != frameIndex || previousTileEntityPos != tileEntity?.pos) {
            previousFrameIndex = frameIndex
            tileEntity?.pos?.let { previousTileEntityPos = it }

            viewMatrix =
                matrixBuffer.apply {
                    rewind()
                    GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this)
                    rewind()
                }.let { Matrix4f(it) }

            projectionMatrix =
                matrixBuffer.apply {
                    rewind()
                    GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this)
                    rewind()
                }.let { Matrix4f(it) }
        }

        val modelMatrix = Matrix4f()
        tileEntity?.pos?.let { modelMatrix.translate(it.x.toFloat(), it.y.toFloat(), it.z.toFloat()) }
        tileEntity?.resourceState?.resourceSet?.config?.offset?.let { modelMatrix.translate(Vector3f(it)) }

        //Cancel previous operations
        viewMatrix.mul(Matrix4f(modelMatrix).invert())

        val lightMapCoords =
            if (tileEntity == null) {
                Vector2f(248.0F / 256.0F, 248.0F / 256.0F)
            } else {
                Vector2f((OpenGlHelper.lastBrightnessX + 8.0F) / 256.0F, (OpenGlHelper.lastBrightnessY + 8.0F) / 256.0F)
            }

        render(tileEntity, pass, tickProgression, modelMatrix, viewMatrix, projectionMatrix, lightMapCoords)
    }

    abstract fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelMatrix: Matrix4f,
        viewMatrix: Matrix4f,
        projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    )
}
