package io.github.ncomet.tournament.interfaces.rest.resources.player

import com.beust.klaxon.Klaxon
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.dropwizard.testing.junit5.ResourceExtension
import io.github.ncomet.tournament.application.player.PlayersService
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.infrastructure.persistence.player.InMemoryAllPlayers
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

@ExtendWith(DropwizardExtensionsSupport::class)
internal class PlayerResourceTest : WithAssertions {

    private val allPlayers: AllPlayers = InMemoryAllPlayers()

    private val resources: ResourceExtension = ResourceExtension.builder()
            .addResource(PlayerResource(PlayersService(allPlayers), allPlayers))
            .build()

    @BeforeEach
    fun setUp() {
        allPlayers.removeAll()
        allPlayers.add(Player(PlayerID("0"), Nickname("piotr"), 10))
        allPlayers.add(Player(PlayerID("1"), Nickname("quentin"), 10))
    }

    @Test
    fun `GET should retrieve a player and its information`() {
        val response = resources
                .target("/api/players/0")
                .request()
                .get()

        assertThat(response.status).isEqualTo(200)
        val entity = response.readEntity(String::class.java)
        val playerRepresentation = Klaxon().parse<RankedPlayerRepresentation>(entity)!!
        assertThat(playerRepresentation.id).isEqualTo("0")
        assertThat(playerRepresentation.nickname).isEqualTo("piotr")
        assertThat(playerRepresentation.score).isEqualTo(10)
        assertThat(playerRepresentation.ranking.rank).isEqualTo(1)
        assertThat(playerRepresentation.ranking.tie).isEqualTo(true)
        assertThat(playerRepresentation._links).hasSize(1)
    }


    @Test
    fun `GET should return 404 if player not found`() {
        val response = resources
                .target("/api/players/123")
                .request()
                .get()

        assertThat(response.status).isEqualTo(404)
    }

    @Test
    fun `PUT should replace player score`() {
        val response = resources
                .target("/api/players/0")
                .request()
                .put(Entity.entity(PlayerBody(70), MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(200)
        val player = allPlayers.byId(PlayerID("0"))!!
        assertThat(player.score).isEqualTo(70)
        assertThat(player.id).isEqualTo(PlayerID("0"))
        assertThat(player.nickname).isEqualTo(Nickname("piotr"))
    }

    @Test
    fun `PUT should return 404 if player not found`() {
        val response = resources
                .target("/api/players/123")
                .request()
                .put(Entity.entity(PlayerBody(70), MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(404)
    }

    @Test
    fun `PUT with null score should return 422`() {
        val response = resources
                .target("/api/players/0")
                .request()
                .put(Entity.entity(PlayerBody(null), MediaType.APPLICATION_JSON_TYPE))

        assertThat(response.status).isEqualTo(422)
    }
}