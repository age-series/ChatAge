package org.ageseries.chatage

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import java.io.File

object ChatAgeConfig {
    private val CONFIG_FILE: File = File("config/chat_age.yaml")
    var config: ChatAgeData = ChatAgeData()

    fun loadConfig(configFile: File = CONFIG_FILE) {
        try {
            if (configFile.isFile) {
                LOGGER.info("[Chat Age] Reading config from ${configFile.absoluteFile}")
                config = Yaml.default.decodeFromStream(ChatAgeData.serializer(), configFile.inputStream())
            } else {
                config = ChatAgeData()
                saveConfig()
            }
        } catch (e: Exception) {
            LOGGER.error("Chat Age had an issue with loading the configuration file, please check the file for errors.")
            LOGGER.error("Check that 1) You have valid YAML 2) the config directives are spelled correctly (see documentation)")
            LOGGER.error(e)
        }
    }

    private fun saveConfig(configFile: File = CONFIG_FILE) {
        try {
            LOGGER.info("[Chat Age] Writing config to ${configFile.absoluteFile}")
            val configText = Yaml.default.encodeToString(ChatAgeData.serializer(), config)
            if (!configFile.exists()) {
                configFile.createNewFile()
            }
            configFile.writeText(configText)
        } catch (e: Exception) {
            LOGGER.error("Chat Age was unable to write back the config file, please check filesystem permissions: $e")
        }
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
