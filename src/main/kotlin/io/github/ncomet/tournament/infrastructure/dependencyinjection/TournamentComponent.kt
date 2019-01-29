package io.github.ncomet.tournament.infrastructure.dependencyinjection

import dagger.Component
import dagger.Module
import dagger.Provides
import io.github.ncomet.tournament.application.player.PlayersService
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.infrastructure.persistence.dynamodb.DynamoDbModule
import io.github.ncomet.tournament.infrastructure.persistence.dynamodb.TablesModule
import io.github.ncomet.tournament.infrastructure.persistence.player.DynamoDbAllPlayersModule
import io.github.ncomet.tournament.interfaces.rest.healthcheck.DynamoDbHealthCheck
import io.github.ncomet.tournament.interfaces.rest.resources.player.PlayerResource
import io.github.ncomet.tournament.interfaces.rest.resources.player.PlayersResource
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DynamoDbModule::class,
    TablesModule::class,
    DynamoDbAllPlayersModule::class,
    PlayersServiceModule::class
])
interface TournamentComponent {
    fun playersResource(): PlayersResource
    fun playerResource(): PlayerResource
    fun dynamoDbHealthCheck(): DynamoDbHealthCheck
}

@Module
class PlayersServiceModule {
    @Provides
    @Singleton
    fun playersService(allPlayers: AllPlayers): PlayersService = PlayersService(allPlayers)
}