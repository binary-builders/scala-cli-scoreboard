package com.wgmouton.scoreboard

//import com.wgmouton.scoreboard.Processor.{loop, printRanking, sortScoreboard}
import wvlet.airframe.launcher._
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
class MyApp(
//           @option(prefix = "-h,--help", description = "display help messages", isHelp = true)
//           help: Boolean = false
         ) {

  @command(isDefault = true)
  def default {
    println("Type --help to display the list of commands")
  }

  //  Logger.setDefaultLogLevel(LogLevel.ERROR)

//  /**
//   * The command required by the project. Provide it filepath or '-' to indicate the use of stdIn and will printout the rankings.
//   *
//   * @param input filepath or '-'
//   */
//  @command(isDefault = true, usage = "[filepath] | [-] for stdIn", description = "calculate rankings for teams in a match")
//  final def rankings(
//                      @argument(name = "input", description = "the path to a file or '-' to read input from stdin")
//                      input: Option[String] = None,
//                    ): Unit = {
//    input.flatMap {
//      // Fetches from stdIn
//      case "-" => Map.empty[String, Int]
//        Option(StdIn.readLine()).map(s => loop[String](s, Map.empty, _ => Option(StdIn.readLine())))
//
//      //Fetches the matches from a file
//      case filepath if Files.exists(Paths.get(filepath)) =>
//        val file = Source.fromFile(filepath)
//        val output = loop[Iterator[String]](file.getLines(), Map.empty, _.nextOption())
//        file.close()
//        output.some
//
//      //Failed to load anything
//      case file =>
//        printf("File %s not found. Please confirm that the file exists or provide the input via stdIn by indicated '-' as the first argument", file)
//        System.exit(1)
//        None
//    } foreach { scoreboard =>
//      printRanking(sortScoreboard(scoreboard))
//    }
//  }

//  /**
//   * Just something cool. This will generate a bush of teams and calculate the rankings for indicated amount of matches.
//   *
//   * @param teams   default is 20
//   * @param matches default is 50
//   */
//  @command(usage = "50 ", description = "calculate rankings for teams in a match")
//  final def random(
//                    @option(prefix = "-t,--teams", description = "the total number of teams")
//                    teams: Int = 20,
//                    @option(prefix = "-m,--matches", description = "the amount of matches")
//                    matches: Int = 50,
//                  ): Unit = {
//    val teamsGroup1 = List.fill(teams / 2)(Random.alphanumeric.take(5).mkString)
//    val teamsGroup2 = List.fill(teams / 2)(Random.alphanumeric.take(5).mkString)
//
//    val input = List.fill(matches) {
//      val m = Match(
//        TeamScore(teamsGroup1(Random.nextInt(teamsGroup1.length)), Random.nextInt(5)),
//        TeamScore(teamsGroup2(Random.nextInt(teamsGroup2.length)), Random.nextInt(5))
//      )
//      String.format("%s %d, %s %d", m.team1.name, m.team1.score, m.team2.name, m.team2.score)
//    }.iterator
//
//    val scoreboard = loop[Iterator[String]](input, Map.empty, _.nextOption())
//    printRanking(sortScoreboard(scoreboard))
//  }
}

import wvlet.airframe.launcher._

//class MyApp(@option(prefix = "-h,--help", description = "display help messages", isHelp = true)
//            help: Boolean = false,
//            @option(prefix = "-p", description = "port number")
//            port: Int = 8080) {
//
//  @command(isDefault = true)
//  def default: Unit = {
//    println(s"Hello airframe. port:${port}")
//  }
//}

object Main extends App {
  Launcher.execute[MyApp]("")
}