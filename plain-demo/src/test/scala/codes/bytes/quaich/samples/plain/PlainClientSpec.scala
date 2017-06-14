package codes.bytes.quaich.samples.plain

import com.amazonaws.services.lambda.AWSLambdaClientBuilder
import com.amazonaws.services.lambda.invoke.{LambdaFunction, LambdaInvokerFactory}
import com.amazonaws.services.lambda.model.InvocationType
import org.scalatest.{MustMatchers, WordSpec}

trait HelloLambdaClient {
  @LambdaFunction(functionName = "plain-demo")
  def fetchNames(): String

  @LambdaFunction(functionName = "plain-demo", invocationType = InvocationType.Event)
  def fetchNamesAsyncNonsense(): Unit
}

class PlainClientSpec  extends WordSpec with MustMatchers {
  "Invoking lambda function" should {
    "work for sync mode" in {

      val helloLambda = LambdaInvokerFactory.builder
        .lambdaClient(AWSLambdaClientBuilder.defaultClient)
        .build(classOf[HelloLambdaClient])

      helloLambda.fetchNames()                // 1
      helloLambda.fetchNamesAsyncNonsense()   // 2

      val start1 = System.currentTimeMillis()
      println(helloLambda.fetchNames())       // 3
      println(s"Execution 1 took: ${System.currentTimeMillis() - start1} ms")

      val start2 = System.currentTimeMillis()
      println(helloLambda.fetchNamesAsyncNonsense()) // 4
      println(s"Execution 2 took: ${System.currentTimeMillis() - start2} ms")
    }
  }
}

