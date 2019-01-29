package io.github.ncomet.tournament.interfaces.rest.resources.player

import io.github.ncomet.tournament.domain.player.Player
import javax.ws.rs.core.UriInfo

data class AllPlayersRepresentation(val players: List<PlayerRepresentation>)
data class PlayerRepresentation(val id: String, val nickname: String, val score: Int, val _links: List<LinkRepresentation>)
data class LinkRepresentation(val rel: String, val href: String)

fun Player.toPlayerRepresentation(uriInfo: UriInfo): PlayerRepresentation {
    val uri = uriInfo.baseUriBuilder.path(PlayerResource::class.java).resolveTemplate("id", id.value).build()
    return PlayerRepresentation(id.value, nickname.value, score, listOf(LinkRepresentation("self", uri.toString())))
}