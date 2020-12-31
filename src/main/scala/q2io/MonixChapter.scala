package q2io

import monix.eval.Coeval
import monix.reactive.Observable
import monix.reactive.Consumer
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import scala.util.Success
import scala.util.Failure

object MonixChapter {
  val lazyNum = Coeval.evalOnce { println(42); 42 }

  val pure0: Observable[Int] = Observable.pure(42)
  val headC: Consumer.Sync[Int, Int] = Consumer.head[Int]
  val task: Task[Int] = pure0.consumeWith(headC)
  task.runToFuture.onComplete {
    case Success(i) => println(s"taks 42 run: $i")
    case Failure(e) => println(s"taks 42 failed with error: $e")
  }
  // task.runToFuture.onSuccess { case i
}
