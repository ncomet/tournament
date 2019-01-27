package io.github.ncomet.tournament

import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Environment
import io.github.ncomet.tournament.infrastructure.dependencyinjection.DaggerTournamentComponent

class TournamentConfig(val name: String = "undefined"): Configuration()

class TournamentApplication(): Application<TournamentConfig>() {
    override fun run(configuration: TournamentConfig, environment: Environment) {
        println("Running Tournament")
        val component = DaggerTournamentComponent.builder().build()
        environment.jersey().register(component.playersResource())
        environment.healthChecks().register("DynamoDbHealthCheck", component.dynamoDbHealthCheck())
    }
}

fun main(args: Array<String>) {
    TournamentApplication().run(*args)
}