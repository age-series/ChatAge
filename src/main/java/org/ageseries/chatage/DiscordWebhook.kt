package org.ageseries.chatage

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import java.io.File
import kotlin.concurrent.thread

fun jsonSafe(s: String) = s.replace("\"", "")

object DiscordWebhook {

    /**
     * @param url: Webhook URL
     * @param data: JSON formatted message data
     */
    private fun httpsPost(url: String, data: String) {
        LOGGER.debug("Sending HTTPS Post request to $url:\n$data")
        val httpClient: HttpClient = HttpClientBuilder.create().build()
        try {
            val request = HttpPost(url)
            val params = StringEntity(data)
            request.addHeader("content-type", "application/json")
            request.entity = params
            val resp = httpClient.execute(request)
            if (resp.statusLine.statusCode > 299) {
                LOGGER.warn(resp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun discordWebhook(message: String, username: String = ChatAgeConfig.config.botName) {
        thread (name = "CA Discord Webhook Thread") {
            var avatar = ""
            if(ChatAgeConfig.config.avatarUrl.isNotBlank()) {
                avatar = ", \"avatar_url\": \"${jsonSafe(ChatAgeConfig.config.avatarUrl)}\""
            }
            val json = """
            {
                "username": "${jsonSafe(username)}",
                "content": "${jsonSafe(message)}"
                $avatar
            }
        """.trimIndent()

            if (ChatAgeConfig.config.webhookUrl.isNotBlank()) {
                httpsPost(ChatAgeConfig.config.webhookUrl, json)
            } else {
                LOGGER.error("[Chat Age] Webhook URL must be defined in the config.")
            }
        }
    }
}

// Sends a test webhook message
fun main() {
    ChatAgeConfig.loadConfig(File("run/config/chat_age.json"))
    DiscordWebhook.discordWebhook("Test message")
}
