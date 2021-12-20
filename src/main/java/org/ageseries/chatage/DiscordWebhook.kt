package org.ageseries.chatage

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import java.io.File


object DiscordWebhook {

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

    fun discordWebhook(message: String, username: String? = ChatAgeConfig.config.botName) {
        val json = """
            {
                "username": "$username",
                "content": "$message"
            }
        """.trimIndent()

        // Currently, not used because I need to come up with a logo to use and a CDN to provide it from.
        // 'avatar_url': '${avatar_url}',

        if (ChatAgeConfig.config.webhookUrl.isNotBlank())
            httpsPost(ChatAgeConfig.config.webhookUrl, json)
        else {
            LOGGER.info("Error! Webhook URL must be defined in the config!")
        }
    }
}

// Sends a test webhook message
fun main() {
    ChatAgeConfig.loadConfig(File("run/config/chat_age.yaml"))
    DiscordWebhook.discordWebhook("Test message")
}
