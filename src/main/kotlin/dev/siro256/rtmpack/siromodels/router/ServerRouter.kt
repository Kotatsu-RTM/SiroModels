package dev.siro256.rtmpack.siromodels.router

import dev.siro256.rtmpack.siromodels.block.platformdoor.ControllerBlock
import dev.siro256.rtmpack.siromodels.block.platformdoor.CrewDoorBlock
import dev.siro256.rtmpack.siromodels.block.platformdoor.MovableDoorBlock
import dev.siro256.rtmpack.siromodels.block.platformdoor.WallBlock
import jp.ngt.ngtlib.block.TileEntityCustom
import jp.ngt.ngtlib.entity.EntityCustom
import jp.ngt.rtm.block.tileentity.TileEntityMachineBase
import jp.ngt.rtm.entity.train.EntityTrainBase
import jp.ngt.rtm.entity.train.EntityTrainElectricCar
import net.minecraft.block.BlockContainer
import net.minecraft.block.state.IBlockState

@Suppress("unused")
object ServerRouter {
    @JvmStatic
    fun onUpdate(entity: TileEntityMachineBase) {
        when (entity.resourceState.resourceName.removePrefix("!SiroModels_")) {
            "door_end_right", "door_end_left", "door_middle_right_front", "door_middle_left_front" ->
                replaceTileEntity(entity, MovableDoorBlock)
            "door_crew" -> replaceTileEntity(entity, CrewDoorBlock)
            "door_controller" -> replaceTileEntity(entity, ControllerBlock)
            "door_wall_1m", "door_wall_2m" -> replaceTileEntity(entity, WallBlock)
        }
    }

    fun onUpdate(entity: EntityTrainBase) {
        when (entity.resourceState.resourceName.removePrefix("!SiroModels_")) {
            "test" -> replaceTrain(entity, EntityTrainElectricCar(entity.world))
        }
    }

    private inline fun <reified T: BlockContainer> replaceTileEntity(
        entity: TileEntityCustom,
        newBlock: T,
        state: IBlockState = newBlock.defaultState
    ) {
        val world = entity.world
        val pos = entity.pos

        if (world.getBlockState(pos).block::class !is T) {
            val tileEntity = newBlock.createTileEntity(world, state)?.apply {
                readFromNBT(entity.writeToNBT(entity.serializeNBT()))
            }?: return

            world.removeTileEntity(pos)
            world.setBlockState(pos, state)
            world.addTileEntity(tileEntity)
        }
    }

    private inline fun <reified T: EntityTrainBase> replaceTrain(entity: EntityCustom, newEntity: T) {
        if (entity !is T) {
            val world = entity.world

            world.removeEntity(entity)
            newEntity.spawnTrain(world)
        }
    }
}
