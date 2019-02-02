package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.domain.rank.Outcome
import io.github.ncomet.tournament.domain.rank.Rank
import io.github.ncomet.tournament.domain.score.AllScores
import io.github.ncomet.tournament.domain.score.PlayerScore
import io.github.ncomet.tournament.infrastructure.persistence.player.InMemoryAllPlayers
import io.github.ncomet.tournament.infrastructure.persistence.score.InMemoryAllScores
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayersServiceTest : WithAssertions {

    private val allScores: AllScores = InMemoryAllScores()
    private val allPlayers: AllPlayers = InMemoryAllPlayers()

    @BeforeEach
    internal fun setUp() {
        allScores.remove()
        allPlayers.remove()
    }

    @Nested
    inner class SortedByScoreDesc {

        @Test
        fun `should retrieve players sorted by score descending`() {
            listOf(
                    Player(PlayerID("0"), Nickname("0"), 0),
                    Player(PlayerID("1"), Nickname("1"), 14),
                    Player(PlayerID("2"), Nickname("2"), 2),
                    Player(PlayerID("3"), Nickname("3"), 32)
            ).forEach {
                allScores.addPlayerScore(PlayerScore(it.score, it.id))
                allPlayers.add(it)
            }

            val playerService = PlayersService(allScores, allPlayers)

            assertThat(playerService.sortedByScoreDesc()).containsExactly(
                    Player(PlayerID("3"), Nickname("3"), 32),
                    Player(PlayerID("1"), Nickname("1"), 14),
                    Player(PlayerID("2"), Nickname("2"), 2),
                    Player(PlayerID("0"), Nickname("0"), 0)
            )
        }

        @Test
        fun `should retrieve all players even if scores are equal`() {
            listOf(
                    Player(PlayerID("johnId"), Nickname("john"), 42),
                    Player(PlayerID("daveId"), Nickname("dave"), 42),
                    Player(PlayerID("georgesId"), Nickname("georges"), 42),
                    Player(PlayerID("michaelId"), Nickname("michael"), 42)
            ).forEach {
                allScores.addPlayerScore(PlayerScore(it.score, it.id))
                allPlayers.add(it)
            }

            val playerService = PlayersService(allScores, allPlayers)

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

    @Nested
    inner class RankPlayer {

        @Test
        fun `should rank players with only unties`() {
            listOf(
                    Player(PlayerID("0"), Nickname("0"), 0),
                    Player(PlayerID("1"), Nickname("1"), 14),
                    Player(PlayerID("2"), Nickname("2"), 2),
                    Player(PlayerID("3"), Nickname("3"), 32),
                    Player(PlayerID("4"), Nickname("4"), 77),
                    Player(PlayerID("5"), Nickname("5"), 1)
            ).forEach {
                allScores.addPlayerScore(PlayerScore(it.score, it.id))
                allPlayers.add(it)
            }

            val playerService = PlayersService(allScores, allPlayers)

            playerService.apply {
                val zero = Player(PlayerID("0"), Nickname("0")).rank()
                val one = Player(PlayerID("1"), Nickname("1")).rank()
                val two = Player(PlayerID("2"), Nickname("2")).rank()
                val three = Player(PlayerID("3"), Nickname("3")).rank()
                val four = Player(PlayerID("4"), Nickname("4")).rank()
                val five = Player(PlayerID("5"), Nickname("5")).rank()
                assertThat(zero.second).isEqualTo(Rank(6, Outcome.UNTIE))
                assertThat(one.second).isEqualTo(Rank(3, Outcome.UNTIE))
                assertThat(two.second).isEqualTo(Rank(4, Outcome.UNTIE))
                assertThat(three.second).isEqualTo(Rank(2, Outcome.UNTIE))
                assertThat(four.second).isEqualTo(Rank(1, Outcome.UNTIE))
                assertThat(five.second).isEqualTo(Rank(5, Outcome.UNTIE))
            }
        }

        @Test
        fun `should rank players with only ties`() {
            listOf(
                    Player(PlayerID("0"), Nickname("0"), 42),
                    Player(PlayerID("1"), Nickname("1"), 42),
                    Player(PlayerID("2"), Nickname("2"), 42),
                    Player(PlayerID("3"), Nickname("3"), 42),
                    Player(PlayerID("4"), Nickname("4"), 42),
                    Player(PlayerID("5"), Nickname("5"), 42)
            ).forEach {
                allScores.addPlayerScore(PlayerScore(it.score, it.id))
                allPlayers.add(it)
            }

            val playerService = PlayersService(allScores, allPlayers)

            playerService.apply {
                val zero = Player(PlayerID("0"), Nickname("0")).rank()
                val one = Player(PlayerID("1"), Nickname("1")).rank()
                val two = Player(PlayerID("2"), Nickname("2")).rank()
                val three = Player(PlayerID("3"), Nickname("3")).rank()
                val four = Player(PlayerID("4"), Nickname("4")).rank()
                val five = Player(PlayerID("5"), Nickname("5")).rank()
                assertThat(zero.second).isEqualTo(Rank(1, Outcome.TIE))
                assertThat(one.second).isEqualTo(Rank(1, Outcome.TIE))
                assertThat(two.second).isEqualTo(Rank(1, Outcome.TIE))
                assertThat(three.second).isEqualTo(Rank(1, Outcome.TIE))
                assertThat(four.second).isEqualTo(Rank(1, Outcome.TIE))
                assertThat(five.second).isEqualTo(Rank(1, Outcome.TIE))
            }
        }

        @Test
        fun `should rank players with ties and unties`() {
            listOf(
                    Player(PlayerID("0"), Nickname("0"), 0),
                    Player(PlayerID("1"), Nickname("1"), 14),
                    Player(PlayerID("2"), Nickname("2"), 1),
                    Player(PlayerID("3"), Nickname("3"), 0),
                    Player(PlayerID("4"), Nickname("4"), 21),
                    Player(PlayerID("5"), Nickname("5"), 14)
            ).forEach {
                allScores.addPlayerScore(PlayerScore(it.score, it.id))
                allPlayers.add(it)
            }

            val playerService = PlayersService(allScores, allPlayers)

            playerService.apply {
                val zero = Player(PlayerID("0"), Nickname("0")).rank()
                val one = Player(PlayerID("1"), Nickname("1")).rank()
                val two = Player(PlayerID("2"), Nickname("2")).rank()
                val three = Player(PlayerID("3"), Nickname("3")).rank()
                val four = Player(PlayerID("4"), Nickname("4")).rank()
                val five = Player(PlayerID("5"), Nickname("5")).rank()
                assertThat(zero.second).isEqualTo(Rank(4, Outcome.TIE))
                assertThat(one.second).isEqualTo(Rank(2, Outcome.TIE))
                assertThat(two.second).isEqualTo(Rank(3, Outcome.UNTIE))
                assertThat(three.second).isEqualTo(Rank(4, Outcome.TIE))
                assertThat(four.second).isEqualTo(Rank(1, Outcome.UNTIE))
                assertThat(five.second).isEqualTo(Rank(2, Outcome.TIE))
            }
        }
    }


}