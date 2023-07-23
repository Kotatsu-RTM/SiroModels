package dev.siro256.rtmpack.siromodels.router

import dev.siro256.rtmpack.siromodels.deepCopy
import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import dev.siro256.rtmpack.siromodels.model.platformdoor.ControllerModel
import dev.siro256.rtmpack.siromodels.model.platformdoor.CrewDoorModel
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.MovableDoorRenderer
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.model.platformdoor.DoorModel
import dev.siro256.rtmpack.siromodels.model.platformdoor.WallModel
import dev.siro256.rtmpack.siromodels.renderer.LightRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.ControllerRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.CrewDoorRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.WallRenderer
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.OrnamentPartsRenderer
import jp.ngt.rtm.render.PartsRenderer

@Suppress("unused")
object RendererRouter {
    @JvmStatic
    fun init(renderer: PartsRenderer<*, *>, modelObject: ModelObject) {
        RenderDataManager.models[renderer.modelName] = when (renderer.modelName.removePrefix("!SiroModels_")) {
            "door_end_right", "door_end_left", "door_middle_right_front", "door_middle_left_front" -> {
                replaceRenderer(
                    modelObject,
                    renderer.deepCopy(MachinePartsRenderer::class.java, MovableDoorRenderer())
                )
                DoorModel(modelObject)
            }

            "door_crew" -> {
                replaceRenderer(
                    modelObject,
                    renderer.deepCopy(MachinePartsRenderer::class.java, CrewDoorRenderer())
                )
                CrewDoorModel(modelObject)
            }

            "door_controller" -> {
                replaceRenderer(
                    modelObject,
                    renderer.deepCopy(MachinePartsRenderer::class.java, ControllerRenderer())
                )
                ControllerModel(modelObject)
            }

            "door_wall_1m", "door_wall_2m" -> {
                replaceRenderer(
                    modelObject,
                    renderer.deepCopy(MachinePartsRenderer::class.java, WallRenderer())
                )
                WallModel(modelObject)
            }

            "light_type1_2m" -> {
                replaceRenderer(
                    modelObject,
                    renderer.deepCopy(OrnamentPartsRenderer::class.java, LightRenderer)
                )
                LightModel(modelObject)
            }

            else -> {
                throw IllegalStateException("Model ${renderer.modelName} isn't handled")
            }
        }
    }

    private inline fun <reified T : PartsRenderer<*, *>> replaceRenderer(modelObject: ModelObject, newRenderer: T) {
        if (modelObject.renderer !is T)
            modelObject::class.java
                .getDeclaredField("renderer")
                .apply { isAccessible = true }
                .set(modelObject, newRenderer)
    }
}
