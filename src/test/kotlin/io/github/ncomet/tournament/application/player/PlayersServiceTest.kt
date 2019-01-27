package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayersServiceTest {

    internal class TestAllPlayers(private val listToReturn: List<Player>) : AllPlayers {
        override fun all(): List<Player> = listToReturn
        override fun add(player: Player): Unit = throw NotImplementedError()
        override fun byId(playerID: PlayerID): Player? = throw NotImplementedError()
        override fun removeAll(): Unit = throw NotImplementedError()
    }

    @Test
    internal fun `should retrieve players sorted by score descending`() {
        val listToReturn = listOf(
                Player(PlayerID("john"), 0),
                Player(PlayerID("dave"), 14),
                Player(PlayerID("georges"), 2),
                Player(PlayerID("michael"), 32)
        )

        val playerService = PlayersService(TestAllPlayers(listToReturn))

        assertThat(playerService.sortedByScoreDesc()).containsExactly(
                Player(PlayerID("michael"), 32),
                Player(PlayerID("dave"), 14),
                Player(PlayerID("georges"), 2),
                Player(PlayerID("john"), 0)
        )
    }

    @Test
    internal fun `should retrieve all players even if scores are equal`() {
        val listToReturn = listOf(
                Player(PlayerID("john"), 42),
                Player(PlayerID("dave"), 42),
                Player(PlayerID("georges"), 42),
                Player(PlayerID("michael"), 42)
        )

        val playerService = PlayersService(TestAllPlayers(listToReturn))

        val players = playerService.sortedByScoreDesc()
        assertThat(players).hasSize(4)
        assertThat(players).contains(
                Player(PlayerID("dave"), 42),
                Player(PlayerID("georges"), 42),
                Player(PlayerID("john"), 42),
                Player(PlayerID("michael"), 42)
        )
    }
}