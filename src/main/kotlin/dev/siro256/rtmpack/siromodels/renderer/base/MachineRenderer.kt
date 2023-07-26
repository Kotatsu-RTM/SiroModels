package dev.siro256.rtmpack.siromodels.renderer.base

import jp.ngt.rtm.block.tileentity.TileEntityMachineBase

abstract class MachineRenderer<T : TileEntityMachineBase> : TileEntityRenderer<T>() {
    override fun modelOffset(tileEntity: T?, modelName: String) =
        super.modelOffset(tileEntity, modelName).also {
            if (tileEntity != null) it.translate(0.5F, 0.0F, 0.5F).rotateY(tileEntity.rotation.toRadians())
        }
}
