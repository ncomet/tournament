package io.github.ncomet.tournament.infrastructure.persistence.player

import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.PlayerID
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations.initMocks

internal class DynamoDbAllPlayersTest : WithAssertions {

    @Mock
    lateinit var mockTable: Table

    @BeforeEach
    internal fun setUp() = initMocks(this)

    @Test
    fun `should map Item to Player`() {
        val player = Item()
                .withString("id", "playerId")
                .withString("nickname", "rob")
                .withInt("score", 19)
                .toPlayer()

        assertThat(player.id).isEqualTo(PlayerID("playerId"))
        assertThat(player.nickname).isEqualTo(Nickname("rob"))
        assertThat(player.score).isEqualTo(19)
    }

    @Nested
    inner class ById {

        @Test
        fun `should retrieve one player`() {
            `when`(mockTable.getItem(anyString(), anyString())).thenReturn(
                    Item()
                            .withString("id", "id")
                            .withString("nickname", "nick")
                            .withInt("score", 32)
            )
            val dynamoDbAllPlayers = DynamoDbAllPlayers(mockTable)

            val player = dynamoDbAllPlayers.byId(PlayerID("id"))
            player?.let {
                assertThat(it.id).isEqualTo(PlayerID("id"))
                assertThat(it.nickname).isEqualTo(Nickname("nick"))
                assertThat(it.score).isEqualTo(32)
            }
        }

        @Test
        fun `should return empty if not found`() {
            `when`(mockTable.getItem(anyString(), anyString())).thenReturn(null)
            val dynamoDbAllPlayers = DynamoDbAllPlayers(mockTable)

            assertThat(dynamoDbAllPlayers.byId(PlayerID("id"))).isNull()
        }
    }

}