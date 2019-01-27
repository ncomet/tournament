package io.github.ncomet.tournament.domain.player

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class PlayerTest {

    @Test
    internal fun `PlayerID should throw IAE when nickname is empty`() {
        val exception = assertThrows(IllegalArgumentException::class.java) { PlayerID(" ") }
        assertThat(exception.message).isEqualTo("PlayerID.nickname should not be empty")
    }

    @Test
    internal fun `should properly instanciate`() {
        val player = Player(PlayerID("myNickname"))

        assertThat(player.id.nickname).isEqualTo("myNickname")
        assertThat(player.score).isEqualTo(0)
    }

    @Test
    internal fun `should properly instanciate with a score`() {
        val player = Player(PlayerID("myNickname"), 42)

        assertThat(player.id.nickname).isEqualTo("myNickname")
        assertThat(player.score).isEqualTo(42)
    }

    @Test
    internal fun `equals is based on PlayerID`() {
        assertThat(Player(PlayerID("peter"), 33)).isEqualTo(Player(PlayerID("peter"), 78))
    }

    @Test
    internal fun `hashcode is based on PlayerID`() {
        assertThat(Player(PlayerID("peter"), 33)).hasSameHashCodeAs(Player(PlayerID("peter"), 78))
    }
}