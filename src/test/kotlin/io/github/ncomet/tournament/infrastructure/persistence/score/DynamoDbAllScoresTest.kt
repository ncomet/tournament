package io.github.ncomet.tournament.infrastructure.persistence.score

import com.amazonaws.services.dynamodbv2.document.Item
import io.github.ncomet.tournament.domain.player.PlayerID
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

internal class DynamoDbAllScoresTest : WithAssertions {

    @Test
    fun `should map to PlayersForScore`() {
        val score = Item().withString("entry", "entry")
                .withInt("score", 3)
                .withStringSet("playerIds", "id1", "id2", "id3")

        val playerForScore = score.toPlayerForScore()

        assertThat(playerForScore.score).isEqualTo(3)
        assertThat(playerForScore.playerIDs).containsExactlyInAnyOrder(PlayerID("id1"), PlayerID("id2"), PlayerID("id3"))
    }

    @Test
    fun `should map to PlayersForScore without playerIds`() {
        val score = Item().withString("entry", "entry")
                .withInt("score", 3)
                .withStringSet("playerIds")

        val playerForScore = score.toPlayerForScore()

        assertThat(playerForScore.score).isEqualTo(3)
        assertThat(playerForScore.playerIDs).isEmpty()
    }

}