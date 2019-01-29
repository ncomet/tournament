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
    And match response.players[0] contains { 'nickname': 'nicolas', score: 0 }

    * def playerId = response.players[0].id

    Given path 'api', 'players', playerId
    And request { score: 77 }
    When method put
    Then status 200

    Given path 'api', 'players', playerId
    When method get
    Then status 200
    And assert response.score == 77

    Given path 'api', 'players'
    When method delete
    Then status 204

    Given path 'api', 'players'
    When method get
    Then status 200
    And assert response.players.length == 0