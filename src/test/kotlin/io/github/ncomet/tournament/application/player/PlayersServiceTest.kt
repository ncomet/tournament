package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

internal class PlayersServiceTest : WithAssertions {

    internal class TestAllPlayers(private val listToReturn: List<Player>) : AllPlayers {
        override fun all(): List<Player> = listToReturn
        override fun add(player: Player): Player = throw NotImplementedError()
        override fun byId(playerID: PlayerID): Player? = throw NotImplementedError()
        override fun removeAll(): Unit = throw NotImplementedError()
    }

    @Test
    fun `should retrieve players sorted by score descending`() {
        val listToReturn = listOf(
                Player(PlayerID("0"), Nickname("0"), 0),
                Player(PlayerID("1"), Nickname("1"), 14),
                Player(PlayerID("2"), Nickname("2"), 2),
                Player(PlayerID("3"), Nickname("3"), 32)
        )

        val playerService = PlayersService(TestAllPlayers(listToReturn))

        assertThat(playerService.sortedByScoreDesc()).containsExactly(
                Player(PlayerID("3"), Nickname("3"), 32),
                Player(PlayerID("1"), Nickname("1"), 14),
                Player(PlayerID("2"), Nickname("2"), 2),
                Player(PlayerID("0"), Nickname("0"), 0)
        )
    }

    @Test
    fun `should retrieve all players even if scores are equal`() {
        val listToReturn = listOf(
                Player(PlayerID("johnId"), Nickname("john"), 42),
                Player(PlayerID("daveId"), Nickname("dave"), 42),
                Player(PlayerID("georgesId"), Nickname("georges"), 42),
                Player(PlayerID("michaelId"), Nickname("michael"), 42)
        )

        val playerService = PlayersService(TestAllPlayers(listToReturn))

        val players = playerService.sortedByScoreDesc()
        assertThat(players).hasSize(4)
        assertThat(players).contains(
                Player(PlayerID("daveId"), Nickname("dave"), 42),
                Player(PlayerID("georgesId"), Nickname("georges"), 42),
                Player(PlayerID("johnId"), Nickname("john"), 42),
                Player(PlayerID("michaelId"), Nickname("micheal"), 42)
        )
    }
}