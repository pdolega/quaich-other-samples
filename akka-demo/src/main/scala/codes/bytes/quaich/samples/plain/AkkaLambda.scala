package codes.bytes.quaich.samples.plain

import java.io.OutputStream

import akka.typed.scaladsl.Actor
import akka.typed.scaladsl.AskPattern._
import akka.typed.{ActorRef, ActorSystem, Behavior}
import akka.util.Timeout
import codes.bytes.quaich.api.LambdaContext
import codes.bytes.quaich.api.direct.{DirectLambda, DirectLambdaHandler}
import org.json4s.JValue
import org.json4s.JsonAST.JString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

sealed trait NameMsg

final case class AddNew(lang: String, replyTo: ActorRef[Array[String]]) extends NameMsg

@DirectLambda
class PlainLambda extends DirectLambdaHandler {
  implicit val timeout = Timeout(1.second)

  val namedBehavior: Behavior[NameMsg] = Actor.mutable[NameMsg](ctx => new NameActor)

  val system: ActorSystem[NameMsg] = ActorSystem("hello", namedBehavior)

  override protected def handleEvent(json: JValue, output: OutputStream)(implicit ctx: LambdaContext) {
    json match {
      case JString(lang) =>
        implicit val scheduler = system.scheduler
        val results: Future[Array[String]] = system ? { ref => AddNew(lang, ref) }
        writeJson (output, Await.result (results, timeout.duration) )

      case other =>
        ctx.log.error(s"Uncreckognized JSON format")
    }
  }
}

class NameActor extends Actor.MutableBehavior[NameMsg] {

  private var names: List[String] = Nil

  override def onMessage(msg: NameMsg): Behavior[NameMsg] = {
    msg match {
      case AddNew(lang, replyTo) =>
        names = lang :: names
        replyTo ! names.toArray
    }

    this
  }
}