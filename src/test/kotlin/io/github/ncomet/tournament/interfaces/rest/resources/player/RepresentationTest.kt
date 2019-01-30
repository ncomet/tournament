package io.github.ncomet.tournament.interfaces.rest.resources.player

import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.domain.player.RankedPlayer
import io.github.ncomet.tournament.domain.rank.Outcome
import io.github.ncomet.tournament.domain.rank.Rank
import org.assertj.core.api.WithAssertions
import org.glassfish.jersey.uri.internal.JerseyUriBuilder
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.ws.rs.core.UriInfo

internal class RepresentationTest : WithAssertions {

    @Test
    fun `should map to represent a player`() {
        val uriInfoMock = mock(UriInfo::class.java)
        `when`(uriInfoMock.baseUriBuilder).thenReturn(JerseyUriBuilder())

        val representation = Player(PlayerID("playerId"), Nickname("joe"), 75)
                .toPlayerRepresentation(uriInfoMock)

        assertThat(representation.id).isEqualTo("playerId")
        assertThat(representation.nickname).isEqualTo("joe")
        assertThat(representation.score).isEqualTo(75)
        assertThat(representation._links).containsExactly(LinkRepresentation("self", "/api/players/playerId"))
    }

    @Test
    fun `should map to represent a ranked player`() {
        val uriInfoMock = mock(UriInfo::class.java)
        `when`(uriInfoMock.baseUriBuilder).thenReturn(JerseyUriBuilder())

        val representation = RankedPlayer(
                Player(PlayerID("playerId"), Nickname("joe"), 75),
                Rank(12, Outcome.UNTIE)
        ).toRankedPlayerRepresentation(uriInfoMock)

        assertThat(representation.id).isEqualTo("playerId")
        assertThat(representation.nickname).isEqualTo("joe")
        assertThat(representation.score).isEqualTo(75)
        assertThat(representation.ranking.rank).isEqualTo(12)
        assertThat(representation.ranking.tie).isEqualTo(false)
        assertThat(representation._links).containsExactly(LinkRepresentation("self", "/api/players/playerId"))

    }
}