package q2io

sealed trait MyList[+T] {
  def ::[TT >: T](el: TT): MyList[TT] = MyCons(el, this)
}
case object MyNil extends MyList[Nothing]
case class MyCons[+T](head: T, tail: MyList[T]) extends MyList[T]

object MyShapelist {
  sealed trait HList
  case object HNil extends HList {
    def ::[F](el: F): HCons[F, HNil.type] = HCons(el, this)

  }
  case class HCons[+H, +T <: HList](head: H, tail: T) extends HList {
    def ::[F](el: F): HCons[F, HCons[H, T]] = HCons(el, this)
  }
}
trait Operation[T] {
  type Result
  def apply(t: T): Result
}

object Ops {
  implicit val strLen = new Operation[String] {
    type Result = Int
    def apply(t: String) = t.length
  }
  implicit val intInc = new Operation[Int] {
    type Result = Int
    def apply(t: Int): Int = t + 1
  }
  def applyOp[T](t: T)(implicit op: Operation[T]): op.Result = op.apply(t)
  type Aux[T0, Result0] = Operation[T0] { type Result = Result0 }
  def applyOps[T, R](
      t: T
  )(implicit op: Aux[T, R], op2: Operation[R]): op2.Result =
    op2.apply(op.apply(t))
}
