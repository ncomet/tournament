package io.github.ncomet.tournament.interfaces.rest.resources.player

import com.beust.klaxon.Klaxon
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.dropwizard.testing.junit5.ResourceExtension
import io.github.ncomet.tournament.application.player.PlayersService
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.domain.score.AllScores
import io.github.ncomet.tournament.domain.score.PlayerScore
import io.github.ncomet.tournament.infrastructure.persistence.player.InMemoryAllPlayers
import io.github.ncomet.tournament.infrastructure.persistence.score.InMemoryAllScores
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

@ExtendWith(DropwizardExtensionsSupport::class)
internal class PlayersResourceTest : WithAssertions {

    private val allPlayers: AllPlayers = InMemoryAllPlayers()
    private val allScores: AllScores = InMemoryAllScores()

    private val resources: ResourceExtension = ResourceExtension.builder()
            .addResource(PlayersResource(PlayersService(allScores, allPlayers)))
            .build()

    @BeforeEach
    fun setUp() {
        allPlayers.remove()
        allScores.remove()
        listOf(
                Player(PlayerID("0"), Nickname("piotr"), 32),
                Player(PlayerID("1"), Nickname("vlad"), 11),
                Player(PlayerID("2"), Nickname("dimitri"), 23),
                Player(PlayerID("3"), Nickname("roel"), 2),
                Player(PlayerID("4"), Nickname("zven"), 123)
        ).forEach {
            allScores.addPlayerScore(PlayerScore(it.score, it.id))
            allPlayers.add(it)
        }
    }

    @Test
    fun `GET should return all players`() {
        val response = resources
                .target("/api/players")
                .request()
                .get()

        assertThat(response.status).isEqualTo(200)
        val entity = response.readEntity(String::class.java)
        val allPlayersRepresentation = Klaxon().parse<AllPlayersRepresentation>(entity)!!
        assertThat(allPlayersRepresentation.players).hasSize(5)
    }

    @Test
    fun `POST should create a new player`() {
        val response = resources
                .target("/api/players")
                .request()
                .post(Entity.entity(PlayerCreateBody("robert"), MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(201)
        val playerId = response.headers["Location"]!![0].toString().split("/").last()
        //assertThat(allPlayers.all()).hasSize(6)
        assertThat(allPlayers.byId(PlayerID(playerId))).isEqualTo(Player(PlayerID(playerId), Nickname("robert"), 0))
    }

    @Test
    fun `POST with null body should return 422`() {
        val response = resources
                .target("/api/players")
                .request()
                .post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(422)
    }

    @Test
    fun `POST with null nickname should return 422`() {
        val response = resources
                .target("/api/players")
                .request()
                .post(Entity.entity(PlayerCreateBody(null), MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(422)
    }

    @Test
    fun `POST with empty nickname should return 422`() {
        val response = resources
                .target("/api/players")
                .request()
                .post(Entity.entity(PlayerCreateBody(""), MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(422)
    }

    @Test
    fun `DELETE should remove all players`() {
        val response = resources
                .target("/api/players")
                .request()
                .delete()

        assertThat(response.status).isEqualTo(204)
        //assertThat(allPlayers.all()).isEmpty()
    }
}