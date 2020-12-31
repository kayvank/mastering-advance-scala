package q2io

import monocle.Prism

sealed trait ConfigValue
case class IntValue(value: Int) extends ConfigValue
case class StringValue(value: String) extends ConfigValue

object ConfigValue {
  val intConfP: Prism[ConfigValue, Int] = Prism[ConfigValue, Int] {
    case IntValue(int) => Some(int)
    case _             => None
  }(IntValue.apply)
}
