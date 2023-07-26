package dev.siro256.rtmpack.siromodels.renderer.base

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.joml.Matrix4fStack
import org.joml.Vector2f

interface Renderer {
    fun Float.toRadians() = Math.toRadians(toDouble()).toFloat()

    fun Matrix4fStack.stack(block: () -> Unit) {
        pushMatrix()
        block.invoke()
        popMatrix()
    }

    fun TileEntity.getLightMapCoordinate(): Vector2f {
        val brightness = world.getCombinedLight(pos, world.getLight(pos))
        return Vector2f(
            (brightness % 65536).toFloat() / 256.0F * 0.9375F + 0.03125F,
            (brightness / 65536).toFloat() / 256.0F * 0.9375F + 0.03125F
        )
    }

    fun getTextureId(textureLocation: ResourceLocation): Int {
        val textureManager = Minecraft.getMinecraft().textureManager
        @Suppress("UNNECESSARY_SAFE_CALL")
        return textureManager.getTexture(textureLocation)?.glTextureId ?: run {
            textureManager.loadTexture(textureLocation, SimpleTexture(textureLocation))
            textureManager.getTexture(textureLocation).glTextureId
        }
    }
}
