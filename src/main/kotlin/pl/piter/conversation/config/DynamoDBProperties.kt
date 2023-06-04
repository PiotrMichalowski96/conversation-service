package pl.piter.conversation.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.dynamodb")
data class DynamoDBProperties(
    val endpoint: String,
    val region: String,
    val accessKey: String,
    val secretKey: String,
)
