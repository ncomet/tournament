package io.github.ncomet.tournament.domain.rank

import io.github.ncomet.tournament.domain.player.Player

data class Rank(val value: Int, val outcome: Outcome) {
    init {
        require(value > 0) { "Rank.value must be strictly greater than 0" }
    }
}

enum class Outcome {
    TIE, UNTIE
}

typealias RankedPlayer = Pair<Player, Rank>