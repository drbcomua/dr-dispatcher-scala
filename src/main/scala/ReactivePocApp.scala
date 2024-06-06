package ua.com.b.dispatcher_scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object ReactivePocApp extends App {
  implicit val system: ActorSystem = ActorSystem("reactive-poc")
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: Materializer = Materializer(system)

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(NameController.route)

  bindingFuture.onComplete {
    case Success(binding) =>
      println(s"Server is online")
      sys.addShutdownHook {
        binding.unbind().onComplete { _ =>
          system.terminate()
        }
      }
    case Failure(exception) =>
      println(s"Failed to bind HTTP endpoint, terminating system: ${exception.getMessage}")
      system.terminate()
  }
}
