# Chat Age

This is Chat Age. It is a Minecraft Mod for 1.18.1

You put a Discord webhook into the config file and it mirrors your Minecraft
chat messages into your Discord chat channel. Pretty simple and effective.

This mod depends on Kotlin for Forge to be installed.

## Config File

See `config/chat_age.yaml` for configuration options. A blank config will be
generated on first start of the server for you.

```yaml
webhookUrl: "https://discord.com/api/webhooks/###/###"
botName: "Your Cool Bot Name"
```

I've redacted my webhook token data but the `###` sections would contain
alphanumeric characters.

## Getting a Discord Webhook URL

1. Click the gear icon to the right of the channel name you want messages to 
be sent to
2. Click the "Integrations" tab on the left side.
3. Click "Webhooks"
4. Click the "New Webhook" button.
5. Optionally, name the webhook bot (it gets overwritten by the mod)
6. Copy the webhook URL and paste it into the config as above.
7. Profit.
