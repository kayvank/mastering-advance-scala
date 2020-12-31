package q2io

import spire.math.Rational
import spire.syntax.literals._
import monocle.Iso

object OpticsAndLenses {
  case class Celsius(value: Rational)
  case class Fahrenheit(value: Rational)
  case class Kelvin(value: Rational)

  def cel2fahr(celsius: Celsius): Fahrenheit = Fahrenheit(
    celsius.value * r"9/5" + 32
  )
  def fahr2cel(fahrenheit: Fahrenheit): Celsius = Celsius(
    (fahrenheit.value - 32) * r"5/9"
  )
  val cel2FahrIso = Iso[Celsius, Fahrenheit](cel2fahr)(fahr2cel)

  def cel2kel(celsius: Celsius): Kelvin = Kelvin(celsius.value + r"273.15")
  def kel2cel(kelvin: Kelvin): Celsius = Celsius(kelvin.value - r"273.15")
  val cel2KelIso = Iso[Celsius, Kelvin](cel2kel)(kel2cel)
  val kel2CelIso: Iso[Kelvin, Celsius] = cel2KelIso.reverse
  val kel2FahrIso: Iso[Kelvin, Fahrenheit] = kel2CelIso composeIso (cel2FahrIso)

  case class Person(
      firstName: String,
      lastName: String,
      age: Int,
      contacts: ContactInfo
  )
  case class ContactInfo(email: String, phone: String)

  val person =
    Person("Joe", "Black", 42, ContactInfo("joe@example.com", "5551234"))

  import monocle.Lens

  // def apply[S, A](get: S => A)(set: A => S => S): Lens[S, A] =

  val emailLens: Lens[Person, String] =
    Lens[Person, String](_.contacts.email)(e =>
      person => person.copy(contacts = person.contacts.copy(email = e))
    )
  val updatedPerson: Person = emailLens.set("joe.black@example.com")(person)

  import monocle.macros.GenLens
  val phoneLens = GenLens[Person](_.contacts.phone)
  val updatedPerson2: Person = phoneLens.set("1234000")(person)
}
