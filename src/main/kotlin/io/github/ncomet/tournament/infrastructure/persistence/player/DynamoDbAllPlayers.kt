package io.github.ncomet.tournament.infrastructure.persistence.player

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import dagger.Module
import dagger.Provides
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.PlayerID
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class DynamoDbAllPlayersModule {
    @Singleton
    @Provides
    fun allPlayers(@Named("players") playersTable: Table): AllPlayers = DynamoDbAllPlayers(playersTable)
}

val PLAYERID_CREATION = PlayerID("FOR_CREATION")

class DynamoDbAllPlayers(private val playersTable: Table) : AllPlayers {
    override fun add(player: Player): Player {
        val newPlayer = player.copy(id = if (PLAYERID_CREATION == player.id) PlayerID(UUID.randomUUID().toString()) else player.id)
        playersTable.updateItem(
                "id",
                newPlayer.id.value,
                AttributeUpdate("nickname").put(newPlayer.nickname.value),
                AttributeUpdate("score").put(newPlayer.score)
        )
        return newPlayer
    }

    override fun all(): List<Player> {
        return playersTable.scan().map { it.toPlayer() }
    }

    override fun byId(playerID: PlayerID): Player? {
        return playersTable.getItem("id", playerID.value)?.toPlayer()
    }

    override fun removeAll() {
        playersTable.scan().forEach {
            playersTable.deleteItem("id", it.getString("id"))
        }
    }
}

fun Item.toPlayer(): Player =
        Player(PlayerID(getString("id")), Nickname(getString("nickname")), getInt("score"))
