package dev.siro256.rtmpack.siromodels.renderer.base

import jp.ngt.rtm.block.tileentity.TileEntityOrnament
import org.joml.Matrix4f

abstract class OrnamentRenderer<T : TileEntityOrnament> : TileEntityRenderer<T>() {
    override fun modelOffset(tileEntity: T?, modelName: String): Matrix4f =
        super.modelOffset(tileEntity, modelName).also { if (tileEntity != null) it.translate(0.5F, 0.5F, 0.5F) }
}
