import cats.implicits._

import scala.annotation.tailrec
import scala.util.Random
case class TeamScore(name: String, score: Int)
case class Match(team1: TeamScore, team2: TeamScore)

val input1 = List(
  Match(TeamScore("Lions", 3), TeamScore("Snakes", 3)),
Match(TeamScore("Tarantulas", 1), TeamScore("FC Awesome", 0)),
Match(TeamScore("Lions", 1), TeamScore("FC Awesome", 1)),
Match(TeamScore("Tarantulas", 3), TeamScore("Snakes", 1)),
)

val teamsGroup1 = List.fill(10)(Random.alphanumeric.take(5).mkString)
val teamsGroup2 = List.fill(10)(Random.alphanumeric.take(5).mkString)


val input2 = List.fill(50)(Match(
    TeamScore(teamsGroup1(Random.nextInt(teamsGroup1.length)), Random.nextInt(5)),
    TeamScore(teamsGroup2(Random.nextInt(teamsGroup2.length)), Random.nextInt(5))
))

@tailrec
def calculateScore(input: List[Match], teamScores: Map[String, Int]): Map[String, Int] = {
  input match {
    case Nil =>
      teamScores
    case Match(TeamScore(t1Name, t1Score), TeamScore(t2Name, t2Score)) :: tail if t1Score > t2Score =>
      calculateScore(tail, teamScores
        .updatedWith(t1Name)(_.fold(3)(_ + 3).some)
        .updatedWith(t2Name)(_.orElse(0.some))
      )
    case Match(TeamScore(t1Name, t1Score), TeamScore(t2Name, t2Score)) :: tail if t1Score < t2Score =>
      calculateScore(tail, teamScores
        .updatedWith(t2Name)(_.fold(3)(_ + 3).some)
        .updatedWith(t1Name)(_.orElse(0.some))
      )
    case Match(TeamScore(t1Name, _), TeamScore(t2Name, _)) :: tail =>
      calculateScore(tail, teamScores
        .updatedWith(t1Name)(_.fold(1)(_ + 1).some)
        .updatedWith(t2Name)(_.fold(1)(_ + 1).some)
      )
  }
}

@tailrec
def printRanking(teamScores: List[(String, Int)], position: Int, lastScore: Int): Unit = {
  teamScores match {
    case Nil =>
    case (name, score) :: tail if score == lastScore =>
      printf("%d. %s, %d pts\n", position, name, score)
      printRanking(tail, position, score)
    case (name, score) :: tail =>
      printf("%d. %s, %d pts\n", position + 1, name, score)
      printRanking(tail, position + 1, score)
  }
}


printRanking(calculateScore(input1, Map.empty).toList.sortWith({
  case ((t1Name, t1Score), (t2Name, t2Score)) if t1Score == t2Score => t1Name < t2Name
  case ((_, t1Score), (_, t2Score)) => t1Score > t2Score
}), 0, -1)

printRanking(calculateScore(input2, Map.empty).toList.sortWith({
  case ((t1Name, t1Score), (t2Name, t2Score)) if t1Score == t2Score => t1Name < t2Name
  case ((_, t1Score), (_, t2Score)) => t1Score > t2Score
}), 0, -1)