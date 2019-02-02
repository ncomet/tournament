package io.github.ncomet.tournament.infrastructure.persistence.score

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import dagger.Module
import dagger.Provides
import io.github.ncomet.tournament.domain.player.PlayerID
import io.github.ncomet.tournament.domain.score.AllScores
import io.github.ncomet.tournament.domain.score.PlayerScore
import io.github.ncomet.tournament.domain.score.PlayersForScore
import io.github.ncomet.tournament.infrastructure.persistence.dynamodb.SCORES_TABLE_NAME
import javax.inject.Named
import javax.inject.Singleton

@Module
class DynamoDbAllScoresModule {
    @Singleton
    @Provides
    fun allScores(@Named(SCORES_TABLE_NAME) scoresTable: Table): AllScores = DynamoDbAllScores(scoresTable)
}

class DynamoDbAllScores(private val scoresTable: Table) : AllScores {

    override fun addPlayerScore(playerScore: PlayerScore) {
        scoresTable.updateItem(
                "entry", "entry",
                "score", playerScore.score,
                AttributeUpdate("playerIds").addElements(playerScore.playerID.value)
        )
    }

    override fun removePlayerScore(playerScore: PlayerScore) {
        scoresTable.updateItem(
                "entry", "entry",
                "score", playerScore.score,
                AttributeUpdate("playerIds").removeElements(playerScore.playerID.value)
        )
    }

    override fun allPlayersForScoreDesc(): List<PlayersForScore> {
        return scoresTable.query(QuerySpec()
                .withHashKey("entry", "entry")
                .withScanIndexForward(false)
        ).filter { it.getStringSet("playerIds").orEmpty().isNotEmpty() }
                .map { it.toPlayerForScore() }
    }

    override fun remove() {
        scoresTable.scan().forEach {
            scoresTable.deleteItem(
                    "entry", it.getString("entry"),
                    "score", it.getNumber("score")
            )
        }
    }

}

fun Item.toPlayerForScore(): PlayersForScore {
    return PlayersForScore(getInt("score"), getStringSet("playerIds").toPlayerIdsSet())
}

private fun Set<String?>?.toPlayerIdsSet(): Set<PlayerID> {
    return this.orEmpty().map { PlayerID(it!!) }.toSet()
}