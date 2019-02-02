package io.github.ncomet.tournament.infrastructure.persistence.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID

class InMemoryAllPlayers : AllPlayers {

    private val database: MutableMap<PlayerID, Player> = mutableMapOf()

    override fun add(player: Player): Player {
        database[player.id] = player
        return player
    }

    override fun byId(playerID: PlayerID): Player? = database[playerID]

    override fun remove() {
        database.clear()
    }

}