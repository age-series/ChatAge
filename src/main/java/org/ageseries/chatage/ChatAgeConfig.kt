package org.ageseries.chatage

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import org.ageseries.chatage.ChatAge.LOGGER
import java.io.File

object ChatAgeConfig {
    private const val CONFIG_PATH: String = "confg/chat_age.yaml"
    var config: ChatAgeData = ChatAgeData()

    fun loadConfig() {
        if(File(CONFIG_PATH).isFile) {
            LOGGER.info("Reading from ${File(CONFIG_PATH).absoluteFile}")
            config = Yaml.default.decodeFromStream(ChatAgeData.serializer(), File(CONFIG_PATH).inputStream())
        } else {
            config = ChatAgeData()
            saveConfig()
        }
    }

    fun saveConfig() {
        LOGGER.info("Writing config to ${File(CONFIG_PATH).absoluteFile}")
        val configText = Yaml.default.encodeToString(ChatAgeData.serializer(), config)
        File(CONFIG_PATH).writeText(configText)
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
