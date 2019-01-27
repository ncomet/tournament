package io.github.ncomet.tournament.infrastructure.persistence.player

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate
import com.amazonaws.services.dynamodbv2.document.Table
import dagger.Module
import dagger.Provides
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import javax.inject.Named
import javax.inject.Singleton

@Module
class DynamoDbAllPlayersModule {
    @Singleton
    @Provides
    fun allPlayers(@Named("players") playersTable: Table): AllPlayers = DynamoDbAllPlayers(playersTable)
}

class DynamoDbAllPlayers(private val playersTable: Table) : AllPlayers {
    override fun add(player: Player) {
        playersTable.updateItem(
                "nickname",
                player.id.nickname, AttributeUpdate("score").addNumeric(player.score)
        )
    }

    override fun all(): List<Player> {
        return playersTable.scan().map { Player(PlayerID(it.getString("nickname")), it.getInt("score")) }
    }

    override fun byId(playerID: PlayerID): Player? {
        val item = playersTable.getItem("nickname", playerID.nickname)
        return Player(PlayerID(item.getString("nickname")), item.getInt("score"))
    }

    override fun removeAll() {
        playersTable.delete()
    }
}