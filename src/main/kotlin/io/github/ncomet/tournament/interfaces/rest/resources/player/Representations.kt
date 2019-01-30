package io.github.ncomet.tournament.interfaces.rest.resources.player

import io.github.ncomet.tournament.domain.player.Player
import io.github.ncomet.tournament.domain.player.RankedPlayer
import io.github.ncomet.tournament.domain.rank.Outcome
import javax.ws.rs.core.UriInfo

data class AllPlayersRepresentation(val players: List<PlayerRepresentation>)
data class PlayerRepresentation(val id: String, val nickname: String, val score: Int, val _links: List<LinkRepresentation>)
data class RankedPlayerRepresentation(val id: String, val nickname: String, val score: Int, val ranking: RankingRepresentation, val _links: List<LinkRepresentation>)
data class RankingRepresentation(val rank: Int, val tie: Boolean)
data class LinkRepresentation(val rel: String, val href: String)

fun Player.toPlayerRepresentation(uriInfo: UriInfo): PlayerRepresentation {
    val id = id.value
    val uri = uriInfo.baseUriBuilder.path(PlayerResource::class.java).resolveTemplate("id", id).build()
    return PlayerRepresentation(id, nickname.value, score, listOf(LinkRepresentation("self", uri.toString())))
}

fun RankedPlayer.toRankedPlayerRepresentation(uriInfo: UriInfo): RankedPlayerRepresentation {
    val id = first.id.value
    val uri = uriInfo.baseUriBuilder.path(PlayerResource::class.java).resolveTemplate("id", id).build()
    return RankedPlayerRepresentation(id, first.nickname.value, first.score,
            RankingRepresentation(second.value, second.outcome == Outcome.TIE),
            listOf(LinkRepresentation("self", uri.toString()))
    )
}