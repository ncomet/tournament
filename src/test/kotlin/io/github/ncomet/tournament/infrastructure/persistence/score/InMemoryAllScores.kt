package io.github.ncomet.tournament.infrastructure.persistence.score

import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.domain.score.AllScores
import io.github.ncomet.tournament.domain.score.PlayerScore
import io.github.ncomet.tournament.domain.score.PlayersForScore
import java.util.*
import kotlin.Comparator


class InMemoryAllScores : AllScores {

    private val database: MutableMap<Int, MutableSet<PlayerID>> = TreeMap(Comparator<Int> { o1, o2 ->
        o2.compareTo(o1)
    })

    override fun addPlayerScore(playerScore: PlayerScore) {
        database.getOrPut(playerScore.score) { mutableSetOf(playerScore.playerID) }.add(playerScore.playerID)
    }

    override fun removePlayerScore(playerScore: PlayerScore) {
        database[playerScore.score]?.remove(playerScore.playerID)
    }

    override fun allPlayersForScoreDesc(): List<PlayersForScore> {
        return database.filterValues { it.isNotEmpty() }.map { PlayersForScore(it.key, it.value) }
    }

    override fun remove() {
        database.clear()
    }
}