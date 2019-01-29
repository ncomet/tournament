package players

import com.intuit.karate.junit5.Karate

class PlayersRunner {

    @Karate.Test
    fun testPlayers(): Karate = Karate().feature("players").relativeTo(javaClass)

}