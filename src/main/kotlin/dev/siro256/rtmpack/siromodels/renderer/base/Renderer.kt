package dev.siro256.rtmpack.siromodels.renderer.base

import org.joml.Matrix4fStack

interface Renderer {
    var currentTexture: Int

    fun Float.toRadians() = Math.toRadians(toDouble()).toFloat()

    fun Matrix4fStack.stack(block: () -> Unit) {
        pushMatrix()
        block.invoke()
        popMatrix()
    }
}
