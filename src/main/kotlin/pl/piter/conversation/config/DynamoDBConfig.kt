package pl.piter.conversation.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
@EnableDynamoDBRepositories(basePackages = ["pl.piter.conversation.repository"])
@EnableConfigurationProperties(value = [DynamoDBProperties::class])
class DynamoDBConfig(private val dynamoDBProperties: DynamoDBProperties) {

    companion object {
        class LocalDateTimeConverter : DynamoDBTypeConverter<Date, LocalDateTime> {
            override fun convert(dateTime: LocalDateTime): Date {
                return Date.from(dateTime.toInstant(ZoneOffset.UTC))
            }

            override fun unconvert(date: Date): LocalDateTime {
                return date.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
            }
        }
    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB = AmazonDynamoDBClientBuilder
        .standard()
        .withEndpointConfiguration(endpointConfig())
        .withCredentials(credentials())
        .build()

    private fun endpointConfig() = AwsClientBuilder.EndpointConfiguration(
        dynamoDBProperties.endpoint,
        dynamoDBProperties.region
    )

    private fun credentials() = AWSStaticCredentialsProvider(
        BasicAWSCredentials(
            dynamoDBProperties.accessKey,
            dynamoDBProperties.secretKey
        )
    )
}