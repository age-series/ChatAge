package org.ageseries.chatage

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import org.ageseries.chatage.ChatAge.LOGGER
import java.io.File

object ChatAgeConfig {
    private val CONFIG_FILE: File = File("config/chat_age.yaml")
    var config: ChatAgeData = ChatAgeData()

    fun loadConfig() {
        if(CONFIG_FILE.isFile) {
            LOGGER.info("Reading from ${CONFIG_FILE.absoluteFile}")
            config = Yaml.default.decodeFromStream(ChatAgeData.serializer(), CONFIG_FILE.inputStream())
        } else {
            config = ChatAgeData()
            saveConfig()
        }
    }

    private fun saveConfig() {
        LOGGER.info("Writing config to ${CONFIG_FILE.absoluteFile}")
        val configText = Yaml.default.encodeToString(ChatAgeData.serializer(), config)
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.createNewFile()
        }
        CONFIG_FILE.writeText(configText)
    }
}

/**
 * ChatAgeData
 *
 * NOTE: ALL FIELDS _MUST_ have default values when called, since the user will likely want a sane default!
 * They may be blank, but MUST be present. Thanks!
 */
@Serializable
data class ChatAgeData(
    val webhookUrl: String = ""
)
