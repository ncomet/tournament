package io.github.ncomet.tournament.interfaces.rest.healthcheck

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.codahale.metrics.health.HealthCheck
import javax.inject.Inject

class DynamoDbHealthCheck @Inject constructor(private val dynamoDB: DynamoDB) : HealthCheck() {
    override fun check(): Result {
        return try {
            dynamoDB.listTables().first()
            Result.healthy()
        } catch (e: Exception) {
            Result.unhealthy(e.message)
        }
    }
}