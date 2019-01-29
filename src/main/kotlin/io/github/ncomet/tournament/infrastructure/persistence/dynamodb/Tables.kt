package io.github.ncomet.tournament.infrastructure.persistence.dynamodb

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.model.*
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

const val PLAYERS_TABLE_NAME = "players"

@Module
class TablesModule {
    @Provides
    @Singleton
    @Named(PLAYERS_TABLE_NAME)
    fun tables(dynamoDB: DynamoDB): Table = Tables(dynamoDB).playersTable()
}

class Tables(private val dynamoDB: DynamoDB) {

    fun playersTable(): Table = dynamoDB.listTables()
            .find { it.tableName == PLAYERS_TABLE_NAME }
            ?: createTable(PLAYERS_TABLE_NAME)

    private fun createTable(name: String): Table {
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
}