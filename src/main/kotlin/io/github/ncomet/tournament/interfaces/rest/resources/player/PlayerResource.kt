package io.github.ncomet.tournament.interfaces.rest.resources.player

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.PlayerID
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/api/players/{id}")
class PlayerResource @Inject constructor(private val allPlayers: AllPlayers) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(@PathParam("id") id: String, @Context uriInfo: UriInfo): Response {
        val player = allPlayers.byId(PlayerID(id))
        return if (player != null) {
            Response.ok(toPlayerRepresentation(player, uriInfo)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun put(@PathParam("id") id: String, body: PlayerBody): Response {
        val player = allPlayers.byId(PlayerID(id))
        return if (player != null) {
            val updatedPlayer = player.copy(score = body.score)
            allPlayers.add(updatedPlayer)
            Response.ok().build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

}

data class PlayerBody @JsonCreator constructor(@JsonProperty("score") val score: Int)
