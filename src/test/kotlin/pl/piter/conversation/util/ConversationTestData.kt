package pl.piter.conversation.util

import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import pl.piter.conversation.domain.model.Conversation

object ConversationTestData {

    private const val filePath = "src/test/resources/"
    private const val jsonFile = ".json"

    private val dbModelSamples = listOf("conversationDbModel", "conversationDbModel_2")
    private val domainSamples = listOf("conversationDomain", "conversationDomain_2")

    fun getDatabaseModel(index: Int): ConversationDbModel = getTestData(dbModelSamples, index)

    fun getDatabaseModels(): List<ConversationDbModel> = getTestDataList(dbModelSamples)

    fun getDomain(index: Int): Conversation = getTestData(domainSamples, index)

    fun getDomains(): List<Conversation> = getTestDataList(domainSamples)

    private inline fun <reified T> getTestData(samples: List<String>, index: Int): T {
        require(index < samples.size)
        val samplePath = samplePath(samples[index])
        return JsonConverter.readJsonFile(samplePath)
    }

    private inline fun <reified T> getTestDataList(samples: List<String>): List<T> = samples
        .map { samplePath(it) }
        .map { JsonConverter.readJsonFile(it) }

    private fun samplePath(sampleName: String) = filePath + sampleName + jsonFile
}