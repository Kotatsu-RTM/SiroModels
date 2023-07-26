package dev.siro256.rtmpack.siromodels.renderer.base

import net.minecraft.tileentity.TileEntity
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
}
