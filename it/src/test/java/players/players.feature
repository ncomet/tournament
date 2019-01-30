Feature: IT Players API

  Background:
    * url 'http://localhost:8080'

  Scenario: Add a player, retrieve it, update score and delete all

    Given path 'api', 'players'
    And request { nickname: 'nicolas' }
    When method post
    Then status 201

    Given path 'api', 'players'
    When method get
    Then status 200
    And match response.players[0] contains { nickname: 'nicolas', score: 0 }
    And assert response.players[0]._links.length == 1

    * def playerId = response.players[0].id

    Given path 'api', 'players', playerId
    And request { score: 77 }
    When method put
    Then status 200

    Given path 'api', 'players', playerId
    When method get
    Then status 200
    And match response contains { nickname: 'nicolas', score: 77, ranking: { rank: 1, tie: false } }
    And assert response._links.length == 1

    Given path 'api', 'players'
    When method delete
    Then status 204

    Given path 'api', 'players'
    When method get
    Then status 200
    And assert response.players.length == 0