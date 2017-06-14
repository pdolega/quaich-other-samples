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

      println(helloLambda.fetchNames())       // 1

      val start1 = System.currentTimeMillis()
      println(helloLambda.fetchNames())       // 2
      println(s"Execution 1 took: ${System.currentTimeMillis() - start1} ms")
    }

    "multiple calls - one after the other" in {
      val helloLambda = LambdaInvokerFactory.builder
        .lambdaClient(AWSLambdaClientBuilder.defaultClient)
        .build(classOf[HelloLambdaClient])

      println(helloLambda.fetchNames())       // 1
      println(helloLambda.fetchNames())       // 2
      println(helloLambda.fetchNames())       // 3
      println(helloLambda.fetchNames())       // 4
      println(helloLambda.fetchNames())       // 5
      println(helloLambda.fetchNames())       // 7
      println(helloLambda.fetchNames())       // 8

      val start2 = System.currentTimeMillis()
      println(helloLambda.fetchNamesAsyncNonsense()) // 9
      println(s"Execution (async) took: ${System.currentTimeMillis() - start2} ms")
    }
  }
}

