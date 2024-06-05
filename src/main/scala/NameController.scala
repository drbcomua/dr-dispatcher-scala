package ua.com.b.dispatcher_scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import spray.json.DefaultJsonProtocol.*
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContext

object NameController {
  implicit val nameFormat: RootJsonFormat[Name] = jsonFormat2(Name.apply)

  implicit val nameListFormat: RootJsonFormat[List[Name]] = listFormat[Name]

  def route(implicit mat: Materializer, ec: ExecutionContext): Route =
    path("name") {
      post {
        entity(as[List[Name]]) { names =>
          onComplete(ReactiveStorage.dispatch(Source(names)).runFold(List.empty[Name])(_ :+ _)) {
            case util.Success(result) => complete(result)
            case util.Failure(ex) => complete(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      } ~
        delete {
          entity(as[List[Name]]) { names =>
            onComplete(ReactiveStorage.remove(Source(names))) {
              case util.Success(_) => complete("Deleted")
              case util.Failure(ex) => complete(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
    }
}
