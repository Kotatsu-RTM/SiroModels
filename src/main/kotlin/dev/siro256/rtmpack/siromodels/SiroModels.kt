package dev.siro256.rtmpack.siromodels

import com.google.gson.GsonBuilder
import dev.siro256.rtmpack.siromodels.block.ornament.BlockLight
import dev.siro256.rtmpack.siromodels.block.ornament.TileEntityLight
import dev.siro256.rtmpack.siromodels.block.platformdoor.*
import dev.siro256.rtmpack.siromodels.renderer.LightRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.ControllerRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.CrewDoorRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.MovableDoorRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.WallRenderer
import dev.siro256.rtmpack.siromodels.sound.Sounds
import jp.ngt.rtm.modelpack.ModelPackManager
import jp.ngt.rtm.modelpack.cfg.ModelConfig
import jp.ngt.rtm.modelpack.cfg.ResourceConfig
import net.minecraft.block.Block
import net.minecraft.util.SoundEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.ProgressManager
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.LogManager
import java.io.File
import java.net.URI
import java.util.zip.ZipFile

@Mod(modid = Values.MOD_ID, name = Values.MOD_NAME, version = Values.MOD_VERSION)
@EventBusSubscriber(modid = Values.MOD_ID)
class SiroModels {
    @EventHandler
    fun fmlInitEvent(event: FMLInitializationEvent) {
        registerModels()

        listOf(
            RightClickHandler
        ).forEach(MinecraftForge.EVENT_BUS::register)

        mapOf(
            MovableDoorTileEntity::class.java to MovableDoorBlock,
            CrewDoorTileEntity::class.java to CrewDoorBlock,
            ControllerTileEntity::class.java to ControllerBlock,
            WallTileEntity::class.java to WallBlock,
            TileEntityLight::class.java to BlockLight
        ).forEach {
            GameRegistry.registerTileEntity(it.key, it.value.registryName)
        }

        if (event.side.isClient) {
            ClientRegistry.bindTileEntitySpecialRenderer(
                MovableDoorTileEntity::class.java,
                MovableDoorRenderer.TileEntityRenderer
            )
            ClientRegistry.bindTileEntitySpecialRenderer(
                CrewDoorTileEntity::class.java,
                CrewDoorRenderer.TileEntityRenderer
            )
            ClientRegistry.bindTileEntitySpecialRenderer(
                ControllerTileEntity::class.java,
                ControllerRenderer.TileEntityRenderer
            )
            ClientRegistry.bindTileEntitySpecialRenderer(
                WallTileEntity::class.java,
                WallRenderer.TileEntityRenderer
            )
            ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityLight::class.java,
                LightRenderer.TileEntityRenderer
            )
        }
    }

    private fun registerModels() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val path =
            javaClass.protectionDomain.codeSource.location.path
                .removePrefix("jar:").split("!")
                .dropLast(1).joinToString("")

        ZipFile(File(URI(path))).use { file ->
            val modelDefinitions =
                file.entries().toList()
                    .filter { it.name.startsWith("assets/siromodels/model_jsons/", true) && !it.isDirectory }

            val progress = ProgressManager.push("Preparing models", modelDefinitions.size)

            modelDefinitions.forEach { entry ->
                progress.step(entry.name.split("/").last())

                val type = ModelPackManager.INSTANCE.getType(entry.name.split("/")[3])
                val json = file.getInputStream(entry).readBytes().decodeToString()
                val resourceConfig = gson.fromJson<ResourceConfig>(json, type.cfgClass)

                if (resourceConfig is ModelConfig) {
                    resourceConfig::class.java
                        .getDeclaredField("name")
                        .apply { isAccessible = true }
                        .set(resourceConfig, "!SiroModels_${resourceConfig.name}")
                }

                ModelPackManager.INSTANCE.registerResourceSet(type, resourceConfig, json)
            }

            ProgressManager.pop(progress)
        }
    }

    companion object {
        val logger = LogManager.getLogger("SiroModels")!!

        @JvmStatic
        @SubscribeEvent
        fun registerBlocks(event: RegistryEvent.Register<Block>) {
            logger.info("Register blocks")

            listOf(
                MovableDoorBlock,
                CrewDoorBlock,
                ControllerBlock,
                WallBlock,
                BlockLight
            ).forEach {
                event.registry.register(it)
            }
        }

        @JvmStatic
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        fun registerSounds(event: RegistryEvent.Register<SoundEvent>) {
            logger.info("Register sounds")
            event.registry.register(Sounds.Machine.Door.ERROR)
        }
    }
}
