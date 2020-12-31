package q2io

import shapeless.ops.hlist.Mapper
import shapeless._

object ShapelessP {

  object upperPoly extends Poly1 {
    implicit def string = at[String](_.toUpperCase)
    implicit def default[A] = at[A](identity)
  }
  def makeUpperCase[T <: HList](list: T)(implicit
      mapper: Mapper.Aux[upperPoly.type, T, T]
  ): mapper.Out =
    list.map(upperPoly)

  type ESI = Exception :+: String :+: Int :+: CNil
  val excESI = Coproduct[ESI](new Exception)
  val strESI = Coproduct[ESI]("string")
  val intESI = Coproduct[ESI](10)

}
