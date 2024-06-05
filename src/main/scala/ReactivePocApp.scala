package ua.com.b.dispatcher_scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object ReactivePocApp extends App {
  implicit val system: ActorSystem = ActorSystem("reactive-poc")
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: Materializer = Materializer(system)

  private val bindingFuture = Http().newServerAt("localhost", 8080).bind(NameController.route)

  println("Server online at http://localhost:8080/")
  StdIn.readLine() // Let it run until user presses return
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
