package codes.bytes.quaich.samples.plain

import com.amazonaws.services.lambda.AWSLambdaClientBuilder
import com.amazonaws.services.lambda.invoke.{LambdaFunction, LambdaInvokerFactory}
import org.scalatest.{MustMatchers, WordSpec}

trait HelloLambdaClient {
  @LambdaFunction(functionName = "akka-demo")
  def fetchNames(lang: String): Array[String]

  @LambdaFunction(functionName = "akka-demo")
  def fetchNamesAsyncNonsense(lang: String): Unit
}

class AkkaClientSpec  extends WordSpec with MustMatchers {
  "Invoking lambda function" should {
    "work for sync mode" in {

      val helloLambda = LambdaInvokerFactory.builder
        .lambdaClient(AWSLambdaClientBuilder.defaultClient)
        .build(classOf[HelloLambdaClient])

      helloLambda.fetchNames("idris")     // 1
      helloLambda.fetchNamesAsyncNonsense("rust")   // 2

      val start1 = System.currentTimeMillis()
      val result = helloLambda.fetchNames("clojure")    // 3
      println(result.mkString(" * ") + s" (number: ${result.length})")
      println(s"Execution 1 took: ${System.currentTimeMillis() - start1} ms")

      val start2 = System.currentTimeMillis()
      println(helloLambda.fetchNamesAsyncNonsense("eliksir"))     // 4
      println(s"Execution 2 took: ${System.currentTimeMillis() - start2} ms")
    }
  }
}

