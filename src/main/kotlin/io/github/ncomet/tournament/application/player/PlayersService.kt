package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.RankedPlayer
import io.github.ncomet.tournament.domain.rank.Outcome
import io.github.ncomet.tournament.domain.rank.Rank

class PlayersService(private val allPlayers: AllPlayers) {

    fun sortedByScoreDesc(): List<Player> = allPlayers.all().sortedByDescending { it.score }

    fun Player.rank(): RankedPlayer {
        var currentRank = 0
        return sortedByScoreDesc()
                .asSequence()
                .mapIndexed { i, player ->
                    currentRank = sortedByScoreDesc().rank(i, currentRank)
                    player to Rank(currentRank, sortedByScoreDesc().outcome(i))
                }
                .first { it.first.id == this.id }
    }

}

private fun List<Player>.rank(index: Int, previousRank: Int): Int {
    val previousScore = this.getOrNull(index - 1)?.score
    val score = this.getOrNull(index)?.score
    return if (previousScore != score) previousRank + 1 else previousRank
}

private fun List<Player>.outcome(index: Int): Outcome {
    val previousScore = this.getOrNull(index - 1)?.score
    val score = this.getOrNull(index)?.score
    val nextScore = this.getOrNull(index + 1)?.score
    return if (score == previousScore || score == nextScore) Outcome.TIE else Outcome.UNTIE
}
