package io.github.ncomet.tournament.interfaces.rest.resources.player

import io.github.ncomet.tournament.application.player.PlayersService
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/players")
class PlayersResource @Inject constructor(private val playersService: PlayersService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(): Response {
      return Response.ok(playersService.sortedByScoreDesc()).build()
    }

}