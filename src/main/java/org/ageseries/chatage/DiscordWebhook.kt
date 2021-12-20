package org.ageseries.chatage

import org.ageseries.chatage.ChatAge.LOGGER
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder


object DiscordWebhook {

    private fun httpsPost(url: String, data: String) {
        val httpClient: HttpClient = HttpClientBuilder.create().build()
        try {
            val request = HttpPost(url)
            val params = StringEntity(data)
            request.addHeader("content-type", "application/json")
            request.entity = params
            httpClient.execute(request)
        } catch (_: Exception) {}
    }

    fun discordWebhook(message: String, username: String? = "Chat Age") {
        val json = """
            {
                'username': '${username}',
                'content': '${message}'
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
