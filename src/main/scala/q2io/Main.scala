package q2io

import cats.instances.string._
import cats.syntax.semigroup._
import q2io.Ch3._
import freeT._
import OpticsAndLenses._
import spire.syntax.literals._
import ConfigValue._

object Main extends App {
  println("Hello " |+| "Cats!")
  val rr = resultFr.foldMap(idInterpreter)

  println(s"0 celsius in fahrenheit = ${kel2FahrIso.get(Kelvin(r"273.15"))}")
  def offsetPort(port: Int) = port + 8000
  val portNumber: ConfigValue = StringValue("9000")
  val updatePort: ConfigValue = intConfP.modify(offsetPort)(portNumber)

  println(s"the updated configValue = ${updatePort}")
  println(s"the email lense: ${emailLens.get(updatedPerson)}")
  println(s"the phone lense: ${phoneLens.get(updatedPerson2)}")

}
