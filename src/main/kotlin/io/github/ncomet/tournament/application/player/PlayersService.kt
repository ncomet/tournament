package io.github.ncomet.tournament.application.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player

class PlayersService(private val allPlayers: AllPlayers) {
    fun sortedByScoreDesc(): List<Player> = allPlayers.all().sortedByDescending { it.score }
}
