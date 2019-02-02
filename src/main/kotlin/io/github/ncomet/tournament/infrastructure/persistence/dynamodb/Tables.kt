package io.github.ncomet.tournament.infrastructure.persistence.dynamodb

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.model.*
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

const val PLAYERS_TABLE_NAME = "players"
const val SCORES_TABLE_NAME = "scores"

@Module
class TablesModule {
    @Provides
    @Singleton
    @Named(PLAYERS_TABLE_NAME)
    fun playersTable(dynamoDB: DynamoDB): Table = Tables(dynamoDB).playersTable()

    @Provides
    @Singleton
    @Named(SCORES_TABLE_NAME)
    fun rankingsTables(dynamoDB: DynamoDB): Table = Tables(dynamoDB).scoresTable()
}

class Tables(private val dynamoDB: DynamoDB) {

    fun scoresTable(): Table = dynamoDB.listTables()
            .find { it.tableName == SCORES_TABLE_NAME }
            ?: createScoresTable(SCORES_TABLE_NAME)

    fun playersTable(): Table = dynamoDB.listTables()
            .find { it.tableName == PLAYERS_TABLE_NAME }
            ?: createPlayersTable(PLAYERS_TABLE_NAME)

    private fun createPlayersTable(name: String): Table {
        val createTableRequest = CreateTableRequest().apply {
            setAttributeDefinitions(
                    listOf(
                            AttributeDefinition("id", ScalarAttributeType.S)
                    )
            )
            setKeySchema(
                    listOf(
                            KeySchemaElement("id", KeyType.HASH)
                    )
            )
            provisionedThroughput = ProvisionedThroughput(5L, 6L)
            tableName = name
        }
        val table = dynamoDB.createTable(createTableRequest)
        table.waitForActive()
        return table
    }

    private fun createScoresTable(name: String): Table {
        val createTableRequest = CreateTableRequest().apply {
            setAttributeDefinitions(
                    listOf(
                            AttributeDefinition("entry", ScalarAttributeType.S),
                            AttributeDefinition("score", ScalarAttributeType.N)
                    )
            )
            setKeySchema(
                    listOf(
                            KeySchemaElement("entry", KeyType.HASH),
                            KeySchemaElement("score", KeyType.RANGE)
                    )
            )
            provisionedThroughput = ProvisionedThroughput(5L, 6L)
            tableName = name
        }
        val table = dynamoDB.createTable(createTableRequest)
        table.waitForActive()
        return table
    }
}