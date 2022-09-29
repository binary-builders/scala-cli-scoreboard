package com.wgmouton.scoreboard

import scala.annotation.tailrec
import scala.util.matching.Regex
import cats.implicits._
import wvlet.log.LogSupport

trait Processor extends LogSupport {
  val matchStringRegex: Regex = "(?<t1n>[a-zA-z0-9]+(?:\\s[a-zA-Z0-9]+)*)\\s(?<t1s>[0-9]+),\\s(?<t2n>[a-zA-z0-9]+(?:\\s[a-zA-Z0-9]+)*)\\s(?<t2s>[0-9]*)".r

  protected def printOutput(text: String): Unit

  /**
   * Parses the raw match string into a match.
   *
   * @return
   */
  protected def parseMatch: String => Option[Match] = {
    case matchStringRegex(t1n, t1s, t2n, t2s) =>
      for (t1s <- t1s.toIntOption; t2s <- t2s.toIntOption) yield Match(TeamScore(t1n, t1s), TeamScore(t2n, t2s))
    case text =>
      logger.error(String.format("failed to parse match: '%s', expected format 'Lions 3, Snakes 3'", text))
      None
  }

  /**
   * Calculates the score for a specific match based the conditions set in the match case.
   *
   * @param currentMatch
   * @param currentScoreboard
   * @return
   */
  protected def calculateScores(currentMatch: Match, currentScoreboard: Map[String, Int]): Map[String, Int] = {
    currentMatch match {
      //Team 1 Wins 3 points and 0 points for team 2
      case Match(TeamScore(t1Name, t1Score), TeamScore(t2Name, t2Score)) if t1Score > t2Score =>
        currentScoreboard
          .updatedWith(t1Name)(_.fold(3)(_ + 3).some)
          .updatedWith(t2Name)(_.orElse(0.some)) //This is required to list the team if they have not yet score any points

      //Team 2 wins 3 points and 0 points for team 1
      case Match(TeamScore(t1Name, t1Score), TeamScore(t2Name, t2Score)) if t1Score < t2Score =>
        currentScoreboard
          .updatedWith(t2Name)(_.fold(3)(_ + 3).some)
          .updatedWith(t1Name)(_.orElse(0.some)) //This is required to list the team if they have not yet score any points

      //Draw thus both teams wins 1 point
      case Match(TeamScore(t1Name, _), TeamScore(t2Name, _)) =>
        currentScoreboard
          .updatedWith(t1Name)(_.fold(1)(_ + 1).some)
          .updatedWith(t2Name)(_.fold(1)(_ + 1).some)
    }
  }

  /**
   * The First loop function to calculate the team's total score based on all their matches
   *
   * @param input      Any input that can be written to supply the nextF function
   * @param scoreboard The scoreboard as the as matches are calculated
   * @param nextF      A lambda function that would provide the next rawMatch string
   * @tparam A Anything really that you can build a loop around.
   * @return the final scoreboard
   */
  @tailrec
  final def loop[A](input: A, scoreboard: Map[String, Int], nextF: A => Option[String]): Map[String, Int] = {
    val updatedScoreboard = for {
      rawMatch <- nextF(input)
      parsedMatch <- parseMatch(rawMatch)
    } yield calculateScores(parsedMatch, scoreboard)

    updatedScoreboard match {
      case None => scoreboard
      case Some(scoreboard) => loop(input, scoreboard, nextF)
    }
  }

  /**
   * Prints the output based on the implementation of the function printOutput.
   * The second recursive loop loops over the sorted scoreboard to print the output in the required format.
   *
   * @param teamScores   sorted list of the scoreboard as a List[(String, Int)]
   * @param index        the index of the teamScores list
   * @param lastPosition the last position of the score
   * @param lastScore    the last score that was supplied
   */
  @tailrec
  final def printRanking(teamScores: List[(String, Int)], useStrictOrdering: Boolean = false, index: Int = 0, lastPosition: Int = 0, lastScore: Int = -1): Unit = {
    val ptOrPts: Int => String = {
      case 1 => "pt"
      case _ => "pts"
    }

    val nextPositionOrIndex = if(useStrictOrdering) lastPosition + 1 else index + 1

    teamScores match {
      case Nil =>
      case (name, score) :: tail if score == lastScore =>
        printOutput(String.format("%d. %s, %d %s", lastPosition, name, score, ptOrPts(score)))
        printRanking(tail, useStrictOrdering, nextPositionOrIndex, lastPosition, score)
      case (name, score) :: tail  =>
        printOutput(String.format("%d. %s, %d %s", nextPositionOrIndex, name, score, ptOrPts(score)))
        printRanking(tail, useStrictOrdering, nextPositionOrIndex, nextPositionOrIndex, score)
    }
  }

  /**
   * Sorts the scoreboard into a list
   *
   * @param scoreboard the unsorted scoreboard
   * @return
   */
  def sortScoreboard(scoreboard: Map[String, Int]): List[(String, Int)] = {
    scoreboard.toList.sortWith({
      case ((t1Name, t1Score), (t2Name, t2Score)) if t1Score == t2Score => t1Name < t2Name
      case ((_, t1Score), (_, t2Score)) => t1Score > t2Score
    })
  }
}

object Processor extends Processor {
  override def printOutput(text: String): Unit = println(text)
}