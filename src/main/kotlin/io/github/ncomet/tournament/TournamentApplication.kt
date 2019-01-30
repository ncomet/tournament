package io.github.ncomet.tournament

import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import io.github.ncomet.tournament.infrastructure.dependencyinjection.DaggerTournamentComponent

object swaggerBundle : SwaggerBundle<TournamentConfig>() {
    override fun getSwaggerBundleConfiguration(configuration: TournamentConfig?): SwaggerBundleConfiguration {
        return SwaggerBundleConfiguration().apply {
            resourcePackage = "io.github.ncomet.tournament.interfaces.rest.resources"
        }
    }
}

class TournamentConfig : Configuration()

class TournamentApplication(): Application<TournamentConfig>() {
    override fun run(configuration: TournamentConfig, environment: Environment) {
        println("Running Tournament")
        val component = DaggerTournamentComponent.builder().build()
        environment.jersey().register(component.playersResource())
        environment.jersey().register(component.playerResource())
        environment.healthChecks().register("DynamoDbHealthCheck", component.dynamoDbHealthCheck())
    }

    override fun initialize(bootstrap: Bootstrap<TournamentConfig>) = bootstrap.addBundle(swaggerBundle)
}

fun main(args: Array<String>) {
    TournamentApplication().run(*args)
}