Feature: test Players API

  Background:
    * url 'http://localhost:8080'

  Scenario: get all players

    Given path 'api', 'players'
    When method get
    Then status 200
