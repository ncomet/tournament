Feature: IT Players API

  Background:
    * url 'http://localhost:8080'

  Scenario: Add a player, retrieve it, update score and delete all

    # create player nicolas
    Given path 'api', 'players'
    And request { nickname: 'nicolas' }
    When method post
    Then status 201

    * def location = responseHeaders['Location'][0]
    * def nicolasId = location.substring(location.lastIndexOf('/') + 1)
    * print nicolasId

    # retrieve all players
    Given path 'api', 'players'
    When method get
    Then status 200
    And match response.players[0] contains { nickname: 'nicolas', score: 0 }
    And assert response.players[0]._links.length == 1

    # give nicolas a score of 77
    Given path 'api', 'players', nicolasId
    And request { score: 77 }
    When method put
    Then status 200

    # verifying nicolas is first
    Given path 'api', 'players', nicolasId
    When method get
    Then status 200
    And match response contains { nickname: 'nicolas', score: 77, ranking: { rank: 1, tie: false } }
    And assert response._links.length == 1

    # adding joe
    Given path 'api', 'players'
    And request { nickname: 'joe' }
    When method post
    Then status 201

    * def location = responseHeaders['Location'][0]
    * def joeId = location.substring(location.lastIndexOf('/') + 1)
    * print joeId

    # give joe a score of 99
    Given path 'api', 'players', joeId
    And request { score: 99 }
    When method put
    Then status 200

    # verify nicolas is now second
    Given path 'api', 'players', nicolasId
    When method get
    Then status 200
    And match response contains { nickname: 'nicolas', score: 77, ranking: { rank: 2, tie: false } }
    And assert response._links.length == 1

    # verify joe is first
    Given path 'api', 'players', joeId
    When method get
    Then status 200
    And match response contains { nickname: 'joe', score: 99, ranking: { rank: 1, tie: false } }
    And assert response._links.length == 1

    # remove all players
    Given path 'api', 'players'
    When method delete
    Then status 204

    # verify there are no more players
    Given path 'api', 'players'
    When method get
    Then status 200
    And assert response.players.length == 0