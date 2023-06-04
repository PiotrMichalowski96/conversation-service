package pl.piter.conversation.entity

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class Message(
    val id: String,
    val content: String,
)
