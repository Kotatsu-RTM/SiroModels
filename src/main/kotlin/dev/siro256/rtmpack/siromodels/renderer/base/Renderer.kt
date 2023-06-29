package dev.siro256.rtmpack.siromodels.renderer.base

import com.github.kotatsu_rtm.kotatsulib.api.gl.VBO
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedShader.Builder.Companion.setModelView
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader
import com.github.kotatsu_rtm.kotatsulib.api.shader.TexturedWithColorShader.Builder.Companion.setModelView
import org.joml.Matrix4f
import org.joml.Matrix4fStack
import org.joml.Vector2f

interface Renderer {
    var currentTexture: Int

    fun Float.toRadians() = Math.toRadians(toDouble()).toFloat()

    fun Matrix4fStack.stack(block: () -> Unit) {
        pushMatrix()
        block.invoke()
        popMatrix()
    }

    fun TexturedShader.Builder<Matrix4f, Int, Int, VBO.VertexNormalUV, Vector2f, Nothing, Nothing, Nothing>.setModelView(
        modelMatrix: Matrix4f,
        viewMatrix: ViewMatrix,
    ) =
        setModelView(Matrix4f(viewMatrix.matrix).mul(modelMatrix), Matrix4f(modelMatrix).invert())

    fun TexturedWithColorShader.Builder<Matrix4f, Int, Int, VBO.VertexNormalUV, Vector2f, Nothing, Nothing, Nothing, Nothing>.setModelView(
        modelMatrix: Matrix4f,
        viewMatrix: ViewMatrix,
    ) =
        setModelView(Matrix4f(viewMatrix.matrix).mul(modelMatrix), Matrix4f(modelMatrix).invert())
}
