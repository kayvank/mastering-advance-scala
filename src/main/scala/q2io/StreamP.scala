package q2io

import fs2._
import cats.effect.{IO, ContextShift}
import scala.concurrent.ExecutionContext

object StreamP {
  val global = ExecutionContext.global
  implicit val contextShiftIO: ContextShift[IO] = IO.contextShift(global)

  val s1 = Stream(1, 2, 3)
  val s2: Stream[Pure, Int] = s1.map(_ + 1).fold(0)(_ + _)
  val chunc: Chunk[Int] = Chunk.seq(Seq(1, 2, 3))
  val stream: Stream[Pure, Int] = Stream.chunk(chunc)
  val repeated = stream.repeat.take(6).chunks.toVector
  def threadName = Thread.currentThread().getName
  val e1 = Stream.eval(IO { println(s"$threadName"); 1 })
  lazy val er1 = e1.repeat.take(3).compile.drain.unsafeRunSync()

  val incPipe: Pipe[Pure, Int, Int] = source => source.map(_ + 1)
  val multPipe: Pipe[Pure, Int, Int] = source => source.map(_ * 11)
  val ss2 = s1 through (incPipe) through (multPipe)

}
