package dev.siro256.rtmpack.siromodels.renderer.base

interface Renderer {
    var currentTexture: Int

    fun Float.toRadians() = Math.toRadians(toDouble()).toFloat()
}
