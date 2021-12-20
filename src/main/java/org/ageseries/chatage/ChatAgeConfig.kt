package org.ageseries.chatage

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import java.io.File

object ChatAgeConfig {
    private val CONFIG_FILE: File = File("config/chat_age.yaml")
    var config: ChatAgeData = ChatAgeData()

    fun loadConfig(configFile: File = CONFIG_FILE) {
        if(configFile.isFile) {
            LOGGER.info("Reading config from ${configFile.absoluteFile}")
            config = Yaml.default.decodeFromStream(ChatAgeData.serializer(), configFile.inputStream())
        } else {
            config = ChatAgeData()
            saveConfig()
        }
    }

    private fun saveConfig(configFile: File = CONFIG_FILE) {
        LOGGER.info("Writing config to ${configFile.absoluteFile}")
        val configText = Yaml.default.encodeToString(ChatAgeData.serializer(), config)
        if (!configFile.exists()) {
            configFile.createNewFile()
        }
        configFile.writeText(configText)
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
    val webhookUrl: String = "",
    val botName: String = "Chat Age Bot"
)
