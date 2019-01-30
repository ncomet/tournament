package io.github.ncomet.tournament.domain.rank

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class RankTest : WithAssertions {

    @Test
    fun `rank should instanciate properly`() {
        val rank = Rank(1, Outcome.UNTIE)

        assertThat(rank.value).isEqualTo(1)
        assertThat(rank.outcome).isEqualTo(Outcome.UNTIE)
    }

    @Test
    fun `rank should throw IAE when rank is negative`() {
        val message = assertThrows(IllegalArgumentException::class.java) { Rank(-1, Outcome.UNTIE) }.message
        assertThat(message).isEqualTo("Rank.value must be strictly greater than 0")
    }

    @Test
    fun `rank should throw IAE when rank is zero`() {
        val message = assertThrows(IllegalArgumentException::class.java) { Rank(0, Outcome.UNTIE) }.message
        assertThat(message).isEqualTo("Rank.value must be strictly greater than 0")
    }
}