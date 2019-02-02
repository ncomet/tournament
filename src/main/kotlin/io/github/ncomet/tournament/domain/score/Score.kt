package io.github.ncomet.tournament.domain.score

import io.github.ncomet.tournament.domain.player.PlayerID

data class PlayerScore(val score: Int, val playerID: PlayerID)
data class PlayersForScore(val score: Int, val playerIDs: Set<PlayerID>)

interface AllScores {
    fun addPlayerScore(playerScore: PlayerScore)
    fun removePlayerScore(playerScore: PlayerScore)
    fun allPlayersForScoreDesc(): List<PlayersForScore>
    fun remove()
}