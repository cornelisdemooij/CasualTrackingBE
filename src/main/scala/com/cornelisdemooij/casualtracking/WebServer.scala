package com.cornelisdemooij.casualtracking

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.concat
import akka.stream.ActorMaterializer
import com.cornelisdemooij.casualtracking.api._

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.io.StdIn
import scala.concurrent.duration._

object WebServer {
  // Needed to run the route:
  implicit val system: ActorSystem = ActorSystem("casual-tracking-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  // Needed for the binding future, to cleanly unbind and shutdown the system:
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def main(args: Array[String]): Unit = {
    val routes = concat(
      DataPointEndpoint.route,
      DataCollectionEndpoint.route
//      PlotEndpoint.route
    )

    val bindingFuture = Http().bindAndHandle(
      routes,
      "localhost",
      8080
    )

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()  // Let it run until user presses return.
    bindingFuture
      .flatMap(_.unbind())  // Trigger unbinding from the port...
      .onComplete(_ => Await.result(system.terminate(), 5.seconds))  // ...and shutdown when done.
  }
}
