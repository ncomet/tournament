package io.github.ncomet.tournament.domain.player

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class PlayerTest : WithAssertions {

    @Test
    fun `PlayerID should throw IAE when nickname is empty`() {
        val exception = assertThrows(IllegalArgumentException::class.java) { PlayerID(" ") }
        assertThat(exception.message).isEqualTo("PlayerID.value should not be empty")
    }

    @Test
    fun `Nickname should throw IAE when nickname is empty`() {
        val exception = assertThrows(IllegalArgumentException::class.java) { Nickname(" ") }
        assertThat(exception.message).isEqualTo("Nickname.value should not be empty")
    }

    @Test
    fun `should properly instanciate`() {
        val player = Player(PlayerID("myId"), Nickname("john"))

        assertThat(player.id.value).isEqualTo("myId")
        assertThat(player.nickname.value).isEqualTo("john")
        assertThat(player.score).isEqualTo(0)
    }

    @Test
    fun `should properly instanciate with a score`() {
        val player = Player(PlayerID("uuid"), Nickname("robert"), 42)

        assertThat(player.id.value).isEqualTo("uuid")
        assertThat(player.nickname.value).isEqualTo("robert")
        assertThat(player.score).isEqualTo(42)
    }

    @Test
    fun `equals is based on PlayerID`() {
        assertThat(Player(PlayerID("id"), Nickname("georges"), 33)).isEqualTo(Player(PlayerID("id"), Nickname("denis"), 78))
    }

    @Test
    fun `hashcode is based on PlayerID`() {
        assertThat(Player(PlayerID("id"), Nickname("georges"), 33)).hasSameHashCodeAs(Player(PlayerID("id"), Nickname("denis"), 78))
    }
}