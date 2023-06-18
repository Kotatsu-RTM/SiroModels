package dev.siro256.rtmpack.siromodels.renderer

import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import jp.ngt.ngtlib.renderer.GLHelper
import jp.ngt.rtm.block.tileentity.TileEntityFluorescent
import jp.ngt.rtm.render.OrnamentPartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11

class LightRenderer: OrnamentPartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as LightModel }

    override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (pass != RenderPass.NORMAL) return
        val modelHeight = 0.15F

        GL11.glPushMatrix()

        if (tileEntity is TileEntityFluorescent) {
            when (tileEntity.dir.toInt()) {
                0 -> GL11.glTranslatef(0.0F, 0.5F - modelHeight, 0.0F)
                1 ->{
                    GL11.glTranslatef(0.0F, 0.0F, 0.5F - modelHeight)
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F)
                }
                2 -> {
                    GL11.glTranslatef(0.0F, -0.5F + modelHeight, 0.0F)
                    GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F)
                }
                3 ->{
                    GL11.glTranslatef(0.0F, 0.0F, -0.5F + modelHeight)
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F)
                }
                4 -> {
                    GL11.glTranslatef(0.0F, 0.5F - modelHeight, 0.0F)
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F)
                }
                5 -> {
                    GL11.glTranslatef(0.5F - modelHeight, 0.0F, 0.0F)
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F)
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F)
                }
                6 -> {
                    GL11.glTranslatef(0.0F, -0.5F + modelHeight, 0.0F)
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F)
                    GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F)
                }
                7 -> {
                    GL11.glTranslatef(-0.5F + modelHeight, 0.0F, 0.0F)
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F)
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F)
                }
            }
        }

        GLHelper.setBrightness(Int.MAX_VALUE)
        model.body.render(this)
        model.light.render(this)

        GL11.glPopMatrix()
    }
}
