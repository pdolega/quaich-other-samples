package codes.bytes.quaich.samples.plain

import java.io.OutputStream

import codes.bytes.quaich.api.LambdaContext
import codes.bytes.quaich.api.direct.{DirectLambda, DirectLambdaHandler}
import org.json4s.JValue

import scala.util.Random


@DirectLambda
class PlainLambda extends DirectLambdaHandler {

  val randomValue = Random.nextLong()

  var value = 0

  override protected def handleEvent(json: JValue, output: OutputStream)(implicit ctx: LambdaContext) {
    Thread.sleep(1500)
    value = value + 1

    writeJson(output, s"Success returned from request: ${ctx.awsRequestId}. " +
      s"Random value is: ${randomValue} " +
      s"remaining ms: ${ctx.remainingTimeInMillis} ms " +
      s"VALUE IS: ${value}")
  }
}