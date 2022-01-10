package org.ageseries.chatage

import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.server.ServerAboutToStartEvent
import net.minecraftforge.event.server.ServerStoppedEvent
import net.minecraftforge.fml.IExtensionPoint
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.network.NetworkConstants.IGNORESERVERONLY
import org.spongepowered.asm.mixin.MixinEnvironment
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

    private var first_reg = true

    private fun registerListeners(event: ServerAboutToStartEvent) {
        LOGGER.info("ChatAge loading...")
        thread (name = "ChatAge Config Loader Thread"){ ChatAgeConfig.loadConfig() }

        if(first_reg) {
            EVENT_BUS.addListener(::chatEvent)
            EVENT_BUS.addListener(::playerLogsIn)
            EVENT_BUS.addListener(::playerLogsOut)

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
            first_reg = false
        }
        LOGGER.info("ChatAge extensions registered.")
    }

    init {
        EVENT_BUS.register(this)
        EVENT_BUS.addListener(::registerListeners)
    }
}
