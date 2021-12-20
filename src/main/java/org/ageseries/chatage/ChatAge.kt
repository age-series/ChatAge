package org.ageseries.chatage

import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.IExtensionPoint
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.network.NetworkConstants.IGNORESERVERONLY
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import java.util.function.BiPredicate
import java.util.function.Supplier

@Mod(ChatAge.MODID)
object ChatAge {
    const val MODID: String = "chatage"
    val LOGGER: Logger = LogManager.getLogger()

    private fun chatEvent(event: ServerChatEvent) {
        LOGGER.info(event.player)
        LOGGER.info(event.username)
        LOGGER.info(event.message)
    }

    private fun playerLogsIn(event: PlayerEvent.PlayerLoggedInEvent) {
        LOGGER.info("Player ${event.player.name} joined")
    }

    private fun playerLogsOut(event: PlayerEvent.PlayerLoggedOutEvent) {
        LOGGER.info("Player ${event.player.name} left")
    }

    init {
        if (DIST.isDedicatedServer) {
            EVENT_BUS.addListener {event: ServerChatEvent -> chatEvent(event)}
            EVENT_BUS.addListener {event: PlayerEvent.PlayerLoggedInEvent -> playerLogsIn(event)}
            EVENT_BUS.addListener {event: PlayerEvent.PlayerLoggedOutEvent -> playerLogsOut(event)}
            EVENT_BUS.register(this)
            ChatAgeConfig.loadConfig()

            val extension = Supplier<IExtensionPoint.DisplayTest>(
                fun(): IExtensionPoint.DisplayTest = IExtensionPoint.DisplayTest(
                    fun(): String { return IGNORESERVERONLY },
                    fun(_: String, _: Boolean): Boolean { return true }
                )
            )
            LOADING_CONTEXT.registerExtensionPoint(
                IExtensionPoint.DisplayTest::class.java,
                extension
            )
        }
    }
}
