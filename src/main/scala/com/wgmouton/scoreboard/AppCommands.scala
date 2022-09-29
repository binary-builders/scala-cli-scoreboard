package com.wgmouton.scoreboard

import com.wgmouton.scoreboard.Processor.{loop, printOutput, printRanking, sortScoreboard}
import wvlet.airframe.launcher.{option, command, argument}
import wvlet.log.{LogLevel, LogSupport, Logger}

import scala.io.{Source, StdIn}
import scala.util.Random
import cats.implicits._

import java.nio.file.{Files, Paths}

/**
 * The is the interface for defining the cli with its commands.
 *
 * @param help
 */
class AppCommands(
                   @option(prefix = "-h,--help", description = "display help messages", isHelp = true)
                   help: Boolean = false,
                   @option(prefix = "-l,--log_level", description = "log level")
                   loglevel: Option[LogLevel] = None
                 ) extends LogSupport {
  Logger.setDefaultLogLevel(loglevel.getOrElse(LogLevel.ERROR))

  /**
   * The command required by the project. Provide it filepath or '-' to indicate the use of stdIn and will printout the rankings.
   *
   * @param input             filepath or '-'
   * @param useStrictOrdering Boolean indicates whether to keep the ranking positions 1, 2, 3, 3, 4 or 1, 2, 3, 3, 5. By default this is false
   */
  @command(usage = "[filepath] | [-] for stdIn", description = "calculate rankings for teams in a match")
  final def rankings(
                      @argument(name = "input", description = "the path to a file or '-' to read input from stdin")
                      input: Option[String] = None,
                      @option(prefix = "-s,--strict", description = "[true/false] indicates whether to keep the ranking positions 1, 2, 3, 3, 4 or 1, 2, 3, 3, 5. By default this is false")
                      useStrictOrdering: Option[Boolean] = None,
                    ): Unit = {
    input.flatMap {
      // Fetches from stdIn
      case "-" =>
        //We provide a empty string here as we will get het the first line from stdIn during the first run of the loop.
        loop[String]("", Map.empty, _ => Option(StdIn.readLine())).some

      //Fetches the matches from a file
      case filepath if Files.exists(Paths.get(filepath)) =>
        val file = Source.fromFile(filepath)
        val output = loop[Iterator[String]](file.getLines(), Map.empty, _.nextOption())
        file.close()
        output.some

      //Failed to load anything
      case file =>
        printf("File %s not found. Please confirm that the file exists or provide the input via stdIn by indicated '-' as the first argument", file)
        System.exit(1)
        None
    } foreach { scoreboard =>
      printRanking(sortScoreboard(scoreboard), useStrictOrdering.getOrElse(false))
    }
  }

  /**
   * Just something cool. This will generate a bush of teams and calculate the rankings for indicated amount of matches.
   *
   * @param teams             default is 20
   * @param matches           default is 50
   * @param useStrictOrdering Boolean indicates whether to keep the ranking positions 1, 2, 3, 3, 4 or 1, 2, 3, 3, 5. By default this is true
   */
  @command(usage = "-t 20 -m 50 ", description = "calculate rankings for random teams with set matches in a match")
  final def random(
                    @option(prefix = "-t,--teams", description = "the total number of teams")
                    teams: Int = 20,
                    @option(prefix = "-m,--matches", description = "the amount of matches")
                    matches: Int = 50,
                    @option(prefix = "-s,--strict", description = "the path to a file or '-' to read input from stdin")
                    useStrictOrdering: Option[Boolean] = None,
                  ): Unit = {

    printOutput(String.format("Matches: %d | Teams: %d\n", matches, teams))
    val teamsGroup1 = List.fill(teams / 2)(Random.alphanumeric.take(5).mkString)
    val teamsGroup2 = List.fill(teams / 2)(Random.alphanumeric.take(5).mkString)

    val input = List.fill(matches) {
      val m = Match(
        TeamScore(teamsGroup1(Random.nextInt(teamsGroup1.length)), Random.nextInt(5)),
        TeamScore(teamsGroup2(Random.nextInt(teamsGroup2.length)), Random.nextInt(5))
      )
      String.format("%s %d, %s %d", m.team1.name, m.team1.score, m.team2.name, m.team2.score)
    }.iterator

    val scoreboard = loop[Iterator[String]](input, Map.empty, _.nextOption())
    printRanking(sortScoreboard(scoreboard), useStrictOrdering = false)
  }
}