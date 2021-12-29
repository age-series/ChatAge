package org.ageseries.chatage

import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.IExtensionPoint
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.network.NetworkConstants.IGNORESERVERONLY
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import java.util.function.Supplier
import kotlin.concurrent.thread

@Mod(ChatAge.MODID)
object ChatAge {
    const val MODID: String = "chatage"

    private fun chatEvent(event: ServerChatEvent) {
        DiscordWebhook.discordWebhook(event.message, event.username)
    }

    private fun playerLogsIn(event: PlayerEvent.PlayerLoggedInEvent) {
        DiscordWebhook.discordWebhook("Player ${event.player.name.contents} joined")
    }

    private fun playerLogsOut(event: PlayerEvent.PlayerLoggedOutEvent) {
        DiscordWebhook.discordWebhook("Player ${event.player.name.contents} left")
    }

    init {
        if (DIST.isDedicatedServer) {
            EVENT_BUS.addListener {event: ServerChatEvent -> chatEvent(event)}
            EVENT_BUS.addListener {event: PlayerEvent.PlayerLoggedInEvent -> playerLogsIn(event)}
            EVENT_BUS.addListener {event: PlayerEvent.PlayerLoggedOutEvent -> playerLogsOut(event)}
            EVENT_BUS.register(this)
            thread (name = "CA Config Loader Thread"){ ChatAgeConfig.loadConfig() }

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
