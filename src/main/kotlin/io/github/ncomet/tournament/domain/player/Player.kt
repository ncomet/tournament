package io.github.ncomet.tournament.domain.player

import io.github.ncomet.tournament.domain.rank.Rank

data class PlayerID(val value: String) {
    init {
        require(value.isNotBlank()) { "PlayerID.value should not be empty" }
    }
}

data class Nickname(val value: String) {
    init {
        require(value.isNotBlank()) { "Nickname.value should not be empty" }
    }
}

data class Player(val id: PlayerID, val nickname: Nickname, val score: Int = 0) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Player
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

interface AllPlayers {
    fun add(player: Player): Player
    fun byId(playerID: PlayerID): Player?
    fun remove()
}

typealias RankedPlayer = Pair<Player, Rank>