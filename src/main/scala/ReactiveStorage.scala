package ua.com.b.dispatcher_scala

import akka.stream.scaladsl.{Sink, Source}
import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}
import akka.stream.Materializer

object ReactiveStorage {
  private val storedNames = TrieMap[Name, Boolean]()

  def dispatch(names: Source[Name, _])(implicit mat: Materializer, ec: ExecutionContext): Source[Name, _] = {
    names.mapAsync(1) { name =>
      if (storedNames.contains(name)) Future.successful(Some(name))
      else {
        storedNames.put(name, true)
        Future.successful(None)
      }
    }.collect {
      case Some(name) => name
    }
  }

  def remove(names: Source[Name, _])(implicit mat: Materializer, ec: ExecutionContext): Future[Unit] = {
    names.runWith(Sink.foreach(name => storedNames.remove(name))).map(_ => ())
  }

  def reset(): Unit = {
    storedNames.clear()
  }
}
