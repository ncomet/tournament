package io.github.ncomet.tournament.domain.player

data class PlayerID(val nickname: String) {
    init {
        require(nickname.isNotBlank()) { "PlayerID.nickname should not be empty" }
    }
}

class Player(val id: PlayerID, val score: Int = 0) {

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
    fun add(player: Player)
    fun all(): List<Player>
    fun byId(playerID: PlayerID): Player?
    fun removeAll()
}