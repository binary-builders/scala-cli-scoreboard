package com.wgmouton.scoreboard

import org.scalatest.funsuite.AnyFunSuite
import Processor._

class TestProcessor extends AnyFunSuite with Processor {

  var outputLog: Seq[String] = Seq.empty

  override def printOutput(text: String): Unit = outputLog = outputLog :+ text

  test("Test that matches are parsed correctly") {
    val parsedMatch = parseMatch("Lions 3, Snakes 3")
    assertResult(Match(TeamScore("Lions", 3), TeamScore("Snakes", 3)))(parsedMatch.get)
  }

  test("Test that parsing and incorrectly format match does not produces and failures but proceeds without that match.") {
    val parsedMatch = parseMatch("fail")
    assertResult(None)(parsedMatch)
  }

  test("Test that scores will be calculated and accumulated correctly") {
    val inputMatch1 = Match(TeamScore("Lions", 3), TeamScore("Snakes", 3))
    val scoreboardAfterMatch1 = calculateScores(inputMatch1, Map.empty)
    assertResult(Map[String, Int]("Lions" -> 1, "Snakes" -> 1))(scoreboardAfterMatch1)

    val inputMatch2 = Match(TeamScore("Lions", 1), TeamScore("Snakes", 3))
    val scoreboardAfterMatch2 = calculateScores(inputMatch2, scoreboardAfterMatch1)
    assertResult(Map[String, Int]("Lions" -> 1, "Snakes" -> 4))(scoreboardAfterMatch2)
  }

  test("Test the recursive loop function to build the scoreboard") {
    val input1 = List(
      Match(TeamScore("Lions", 3), TeamScore("Snakes", 3)),
      Match(TeamScore("Tarantulas", 1), TeamScore("FC Awesome", 0)),
      Match(TeamScore("Lions", 1), TeamScore("FC Awesome", 1)),
      Match(TeamScore("Tarantulas", 3), TeamScore("Snakes", 1)),
      Match(TeamScore("Lions", 4), TeamScore("Grouches", 0)),
    ).map(m => String.format("%s %d, %s %d", m.team1.name, m.team1.score, m.team2.name, m.team2.score))

    val expectedScoreboard = Map(
      "Tarantulas" -> 6,
      "Lions" -> 5,
      "FC Awesome" -> 1,
      "Snakes" -> 1,
      "Grouches" -> 0,
    )

    val scoreboard = loop[Iterator[String]](input1.iterator, Map.empty, _.nextOption())
    assertResult(expectedScoreboard)(scoreboard)
  }

  test("Test the sorting functionality") {
    val expectedScoreboardList = List(
      "Tarantulas" -> 6,
      "Lions" -> 5,
      "FC Awesome" -> 1,
      "Snakes" -> 1,
      "Grouches" -> 0,
    )

    val inputScoreboard = Map(
      "FC Awesome" -> 1,
      "Lions" -> 5,
      "Grouches" -> 0,
      "Snakes" -> 1,
      "Tarantulas" -> 6,
    )

    val sortedScoreboard = sortScoreboard(inputScoreboard)
    assertResult(expectedScoreboardList)(sortedScoreboard)
  }

  test("The test output matches the requirements (No strict ordering)") {
    outputLog = Seq.empty
    val expectedOutput = Seq(
      "1. Tarantulas, 6 pts",
      "2. Lions, 5 pts",
      "3. Bobs, 1 pt",
      "3. FC Awesome, 1 pt",
      "3. Snakes, 1 pt",
      "6. Grouches, 0 pts",
    )

    val inputScoreboard = Map(
      "FC Awesome" -> 1,
      "Lions" -> 5,
      "Grouches" -> 0,
      "Snakes" -> 1,
      "Bobs" -> 1,
      "Tarantulas" -> 6,
    )

    printRanking(inputScoreboard.toList.sortWith({
      case ((t1Name, t1Score), (t2Name, t2Score)) if t1Score == t2Score => t1Name < t2Name
      case ((_, t1Score), (_, t2Score)) => t1Score > t2Score
    }), useStrictOrdering = false, 0, 0,-1)

    assertResult(expectedOutput)(outputLog)
  }

  test("The test output matches the requirements (Strict ordering)") {
    outputLog = Seq.empty
    val expectedOutput = Seq(
      "1. Tarantulas, 6 pts",
      "2. Lions, 5 pts",
      "3. Bobs, 1 pt",
      "3. FC Awesome, 1 pt",
      "3. Snakes, 1 pt",
      "4. Grouches, 0 pts",
    )

    val inputScoreboard = Map(
      "FC Awesome" -> 1,
      "Lions" -> 5,
      "Grouches" -> 0,
      "Snakes" -> 1,
      "Bobs" -> 1,
      "Tarantulas" -> 6,
    )

    printRanking(inputScoreboard.toList.sortWith({
      case ((t1Name, t1Score), (t2Name, t2Score)) if t1Score == t2Score => t1Name < t2Name
      case ((_, t1Score), (_, t2Score)) => t1Score > t2Score
    }), useStrictOrdering = true, 0, 0,-1)

    assertResult(expectedOutput)(outputLog)
  }

}
