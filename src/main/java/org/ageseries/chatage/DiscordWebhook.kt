package org.ageseries.chatage

import java.io.File
import kotlin.concurrent.thread

object DiscordWebhook {

    /**
     * @param url: Webhook URL
     * @param data: JSON formatted message data
     *
     * I hate that this uses CURL, but I want _something_ that works. I can't get shadow to work, so no libraries...
     */
    private fun httpsPost(url: String, data: String) {
        /*
        curl -X POST -H "Content-Type: application/json" \
    -d '{"name": "linuxize", "email": "linuxize@example.com"}' \
    https://example/contact
         */

        val commandList = listOf("curl", "-X", "POST", "-H", "Content-Type: application/json", "-d", data, url)
        try {
            val processBuilder = ProcessBuilder(commandList)
            val process = processBuilder.start()
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
    TODO: Uncomment this function and delete the above one once we have shadow working.

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
    */

    fun discordWebhook(message: String, username: String = ChatAgeConfig.config.botName) {
        thread (name = "CA Discord Webhook Thread") {
            val json = """
            {
                "username": "${username.replace("\"", "")}",
                "content": "${message.replace("\"", "")}"
            }
        """.trimIndent()

            // Currently, not used because I need to come up with a logo to use and a CDN to provide it from.
            // 'avatar_url': '${avatar_url}',

            if (ChatAgeConfig.config.webhookUrl.isNotBlank())
                httpsPost(ChatAgeConfig.config.webhookUrl, json)
            else {
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
