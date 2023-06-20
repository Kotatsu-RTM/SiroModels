package dev.siro256.rtmpack.siromodels.renderer.base

import dev.siro256.rtmpack.siromodels.CustomModelObject
import dev.siro256.rtmpack.siromodels.deepCopy
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
import org.lwjgl.opengl.GL11
import kotlin.properties.Delegates

abstract class CustomMachinePartsRenderer : MachinePartsRenderer(), Renderer {
    private var previousFrameIndex = Int.MIN_VALUE
    private var previousTileEntityPos = BlockPos(100_000_000, 100_000_000, 100_000_000)

    private var modelViewMatrix by Delegates.notNull<Matrix4f>()
    private var projectionMatrix by Delegates.notNull<Matrix4f>()
    private val matrixBuffer = GLAllocation.createDirectFloatBuffer(16)

    final override var currentTexture = -1

    @Suppress("DuplicatedCode")
    final override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (currentTexture == -1) {
            modelSet.modelObj = modelSet.modelObj.deepCopy(ModelObject::class.java, CustomModelObject())
            currentTexture = 0
        }

        val frameIndex = Minecraft.getMinecraft().frameTimer.index

        if (previousFrameIndex != frameIndex || previousTileEntityPos != tileEntity?.pos) {
            previousFrameIndex = frameIndex
            tileEntity?.pos?.let { previousTileEntityPos = it }

            modelViewMatrix =
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

        val lightMapCoords =
            if (tileEntity == null) {
                Vector2f(248.0F / 256.0F, 248.0F / 256.0F)
            } else {
                Vector2f((OpenGlHelper.lastBrightnessX + 8.0F) / 256.0F, (OpenGlHelper.lastBrightnessY + 8.0F) / 256.0F)
            }

        render(tileEntity, pass, tickProgression, modelViewMatrix, projectionMatrix, lightMapCoords)
    }

    abstract fun render(
        tileEntity: TileEntity?,
        pass: RenderPass,
        tickProgression: Float,
        modelViewMatrix: Matrix4f,
        projectionMatrix: Matrix4f,
        lightMapCoords: Vector2f,
    )
}
