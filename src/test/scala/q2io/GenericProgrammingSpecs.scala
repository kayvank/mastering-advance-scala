package q2io

import org.specs2._
import shapeless._
import ShapelessP.upperPoly

class GenericProgrammingSpecs extends Specification with ScalaCheck {
  def is = s2"""

Mastering Advancec Scala Chapter Specs
    MyList impl supports Int list  $e1
    HList impl retains type information $e2
    Operaton string length impl $e3
    implicit resolution ops $e4
    implicit resolution applying 2 ops $e5
    use polymorphic function on hlist $e6
    use polymorphic function on as functions $e7
    HList append ops  $e8
"""

  def e1 = {
    val ints = 3 :: 2 :: MyNil
    println(s" myints are : ${ints}")
    ints.toString === "MyCons(3,MyCons(2,MyNil))"
  }
  def e2 = prop { (s: String) =>
    val hlist: MyShapelist.HCons[Int, MyShapelist.HCons[
      String,
      MyShapelist.HNil.type
    ]] = 3 :: s :: MyShapelist.HNil
    hlist.tail.head === s
  }

  import Ops._

  def e3 = prop { (s: String) => strLen(s) === s.length }

  def e4 = prop { (s: String, i: Int) =>
    applyOp(s) === s.length
    applyOp(i) === (i + 1)
  }
  def e5 = prop { (s: String) => applyOps(s) === s.length + 1 }

  def e6 = prop { (s: String) =>
    (1 :: s :: 20 :: HNil).map(upperPoly) === 1 :: s.toUpperCase :: 20 :: HNil
  }

  import ShapelessP._
  def e7 = prop { (s: String) =>
    makeUpperCase(1 :: s :: 20 :: HNil) === (1 :: s.toUpperCase :: 20 :: HNil)
  }

  def e8 = prop { (s: String, i: Int) =>
    val h1 = (1 :: s :: HNil)
    val h2 = h1 :+ i
    h2 === (1 :: s :: i :: HNil)
  }
  def e9 =
    strESI.select[String] must beSome("string")
}
