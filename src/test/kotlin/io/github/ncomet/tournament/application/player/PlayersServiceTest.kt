package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.domain.rank.Outcome
import io.github.ncomet.tournament.domain.rank.Rank
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayersServiceTest : WithAssertions {

    internal class TestAllPlayers(private val listToReturn: List<Player>) : AllPlayers {
        override fun all(): List<Player> = listToReturn
        override fun add(player: Player): Player = throw NotImplementedError()
        override fun byId(playerID: PlayerID): Player? = throw NotImplementedError()
        override fun removeAll(): Unit = throw NotImplementedError()
    }

    @Nested
    inner class SortedByScoreDesc {

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

    @Nested
    inner class RankPlayer {

        @Test
        fun `should rank players with only unties`() {
            val players = listOf(
                    Player(PlayerID("0"), Nickname("0"), 0),
                    Player(PlayerID("1"), Nickname("1"), 14),
                    Player(PlayerID("2"), Nickname("2"), 2),
                    Player(PlayerID("3"), Nickname("3"), 32),
                    Player(PlayerID("4"), Nickname("4"), 77),
                    Player(PlayerID("5"), Nickname("5"), 1)
            )

            val playerService = PlayersService(TestAllPlayers(players))

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
            val players = listOf(
                    Player(PlayerID("0"), Nickname("0"), 42),
                    Player(PlayerID("1"), Nickname("1"), 42),
                    Player(PlayerID("2"), Nickname("2"), 42),
                    Player(PlayerID("3"), Nickname("3"), 42),
                    Player(PlayerID("4"), Nickname("4"), 42),
                    Player(PlayerID("5"), Nickname("5"), 42)
            )

            val playerService = PlayersService(TestAllPlayers(players))

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
            val players = listOf(
                    Player(PlayerID("0"), Nickname("0"), 0),
                    Player(PlayerID("1"), Nickname("1"), 14),
                    Player(PlayerID("2"), Nickname("2"), 1),
                    Player(PlayerID("3"), Nickname("3"), 0),
                    Player(PlayerID("4"), Nickname("4"), 21),
                    Player(PlayerID("5"), Nickname("5"), 14)
            )

            val playerService = PlayersService(TestAllPlayers(players))

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