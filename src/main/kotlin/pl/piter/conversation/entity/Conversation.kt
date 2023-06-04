package pl.piter.conversation.entity

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import pl.piter.conversation.config.DynamoDBConfig
import java.time.LocalDateTime

@DynamoDBTable(tableName = "conversations")
data class Conversation(

    @field:DynamoDBHashKey
    @field:DynamoDBAttribute(attributeName = "conversation_id")
    val id: String,

    @field:DynamoDBAttribute(attributeName = "user_id")
    @field:DynamoDBIndexHashKey(globalSecondaryIndexName = "conversation_user_id_created_at_index")
    val userId: String,

    @field:DynamoDBAttribute(attributeName = "name")
    val name: String,

    @field:DynamoDBAttribute(attributeName = "messages")
    val messages: List<Message>,

    @field:DynamoDBAttribute(attributeName = "created_at")
    @field:DynamoDBIndexRangeKey(globalSecondaryIndexName = "conversation_user_id_created_at_index")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)