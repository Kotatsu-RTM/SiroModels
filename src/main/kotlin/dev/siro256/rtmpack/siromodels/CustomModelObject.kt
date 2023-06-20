package dev.siro256.rtmpack.siromodels

import dev.siro256.rtmpack.siromodels.renderer.base.Renderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.RenderPass
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.SimpleTexture

class CustomModelObject : ModelObject(null, null) {
    override fun renderWithTexture(entity: Any?, pass: RenderPass, tickProgression: Float) {
        if (renderer !is Renderer) return

        textures.forEach {
            val texture =
                when {
                    pass == RenderPass.NORMAL -> {
                        it.material.texture
                    }

                    pass == RenderPass.TRANSPARENT -> {
                        if (!it.doAlphaBlend) return@forEach
                        it.material.texture
                    }

                    pass != RenderPass.LIGHT && pass != RenderPass.LIGHT_FRONT && pass != RenderPass.LIGHT_BACK -> {
                        it.material.texture
                    }

                    else -> {
                        if (!it.doLighting) return@forEach

                        if (it.subTextures != null) {
                            it.subTextures[pass.id - 2]
                        } else {
                            it.material.texture
                        }
                    }
                }

            renderer.currentMatId = it.material.id.toInt()

            val textureManager = Minecraft.getMinecraft().textureManager

            @Suppress("UNNECESSARY_SAFE_CALL")
            (renderer as Renderer).currentTexture =
                textureManager.getTexture(texture)?.glTextureId ?: run {
                    textureManager.loadTexture(texture, SimpleTexture(texture))
                    textureManager.getTexture(texture).glTextureId
                }

            renderer.render(entity, pass, tickProgression)
        }
    }
}
