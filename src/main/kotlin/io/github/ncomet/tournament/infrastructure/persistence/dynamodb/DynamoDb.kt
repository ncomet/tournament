package io.github.ncomet.tournament.infrastructure.persistence.dynamodb

import com.amazonaws.client.builder.AwsClientBuilder.*
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DynamoDbModule {

    @Provides
    @Singleton
    fun dynamoDB(): DynamoDB {
        val dynamoDbURI = System.getenv("DYNAMODB_URI") ?: "http://localhost:4569"
        val dynamoDbRegion = System.getenv("DYNAMODB_REGION") ?: "us-east-1"
        return DynamoDB(
                AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        EndpointConfiguration(dynamoDbURI, dynamoDbRegion)
                )
                .build()
        )
    }

}