package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.RankedPlayer
import io.github.ncomet.tournament.domain.rank.Outcome
import io.github.ncomet.tournament.domain.rank.Rank
import io.github.ncomet.tournament.domain.score.AllScores
import io.github.ncomet.tournament.domain.score.PlayerScore

class PlayersService(private val allScores: AllScores, private val allPlayers: AllPlayers) {

    fun addPlayer(player: Player): Player {
        val existingPlayer = allPlayers.byId(player.id)
        return if (existingPlayer == null) {
            addPlayerAndScore(player)
        } else {
            allScores.removePlayerScore(PlayerScore(existingPlayer.score, player.id))
            addPlayerAndScore(player)
        }
    }

    fun sortedByScoreDesc(): List<Player> {
        return allScores.allPlayersForScoreDesc()
                .flatMap { it.playerIDs }
                .map { allPlayers.byId(it)!! }
    }

    fun Player.rank(): RankedPlayer {
        val player = allPlayers.byId(this.id)!!
        val rank = allScores.allPlayersForScoreDesc()
                .mapIndexed { i, playersForScore ->
                    val playerIDs = playersForScore.playerIDs
                    Rank(i + 1, if (playerIDs.size > 1) Outcome.TIE else Outcome.UNTIE) to playerIDs
                }.first { it.second.contains(player.id) }.first
        return RankedPlayer(player, rank)
    }

    fun removeAll() {
        allPlayers.remove()
        allScores.remove()
    }

    private fun addPlayerAndScore(player: Player): Player {
        allScores.addPlayerScore(PlayerScore(player.score, player.id))
        allPlayers.add(player)
        return player
    }

}
