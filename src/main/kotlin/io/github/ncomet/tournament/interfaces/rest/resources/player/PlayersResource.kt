package io.github.ncomet.tournament.interfaces.rest.resources.player

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.ncomet.tournament.application.player.PlayersService
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.Nickname
import io.github.ncomet.tournament.domain.player.PLAYERID_CREATION
import io.github.ncomet.tournament.domain.player.Player
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/api/players")
class PlayersResource @Inject constructor(private val playersService: PlayersService, private val allPlayers: AllPlayers) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(@Context uriInfo: UriInfo): Response {
        return Response.ok(AllPlayersRepresentation(playersService.sortedByScoreDesc().map { toPlayerRepresentation(it, uriInfo) })).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun post(@Context uriInfo: UriInfo, body: PlayerCreateBody): Response {
        val player = allPlayers.add(Player(PLAYERID_CREATION, Nickname(body.nickname)))
        val location = uriInfo.absolutePathBuilder.path(player.id.value).build()
        return Response.created(location).build()
    }

    @DELETE
    fun delete(): Response {
        allPlayers.removeAll()
        return Response.noContent().build()
    }
}

data class PlayerCreateBody @JsonCreator constructor(@JsonProperty("nickname") val nickname: String)
