package io.github.ncomet.tournament.infrastructure.persistence.player

import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import java.util.*

class InMemoryAllPlayers : AllPlayers {

    private val database: MutableMap<PlayerID, Player> = mutableMapOf()

    override fun add(player: Player): Player {
        val newPlayer = player.copy(id = if (PLAYERID_CREATION == player.id) PlayerID(UUID.randomUUID().toString()) else player.id)
        database[newPlayer.id] = newPlayer
        return newPlayer
    }

    override fun all(): List<Player> = database.values.toList()

    override fun byId(playerID: PlayerID): Player? = database[playerID]

    override fun removeAll() {
        database.clear()
    }

}