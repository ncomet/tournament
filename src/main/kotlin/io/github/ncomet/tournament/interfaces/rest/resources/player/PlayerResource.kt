package io.github.ncomet.tournament.interfaces.rest.resources.player

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.ncomet.tournament.application.player.PlayersService
import io.github.ncomet.tournament.domain.player.AllPlayers
import io.github.ncomet.tournament.domain.player.PlayerID
import io.swagger.annotations.Api
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/api/players/{id}")
@Api(tags = ["/api/players/{id}"], description = "Resource for managing a single Player")
@Produces(MediaType.APPLICATION_JSON)
class PlayerResource @Inject constructor(private val playersService: PlayersService, private val allPlayers: AllPlayers) {

    @GET
    fun get(@PathParam("id") id: String, @Context uriInfo: UriInfo): Response {
        val player = allPlayers.byId(PlayerID(id))
        return if (player != null) {
            with(playersService) {
                Response.ok(player.rank().toRankedPlayerRepresentation(uriInfo)).build()
            }
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun put(@PathParam("id") id: String, @NotNull @Valid body: PlayerBody): Response {
        val player = allPlayers.byId(PlayerID(id))
        return if (player != null) {
            val updatedPlayer = player.copy(score = body.score!!)
            playersService.addPlayer(updatedPlayer)
            Response.ok().build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

}

data class PlayerBody @JsonCreator constructor(@field:NotNull @JsonProperty("score") val score: Int?)
