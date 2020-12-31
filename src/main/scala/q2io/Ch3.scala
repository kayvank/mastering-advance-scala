package q2io

import cats._
import cats.implicits._
import cats.data._
import scala.util.Random
import scala.io.StdIn

object Ch3 {
  val toUpper = Reader((str: String) => str.toUpperCase)
  val greet = Reader((name: String) => s"Hello $name")
  val com1 = toUpper.compose(greet)
  val result = com1.run("Joe")
  println(s"result = $result")

  class AuthService {
    def isLogged(name: String): Boolean = name.length == 3
  }
  class UserService {
    def greet(name: String, isLogged: Boolean): String = {
      val aName = if (isLogged) name; else "User"
      s"Hello $aName"

    }
  }
  case class Environment(
      userName: String,
      userService: UserService,
      authService: AuthService
  )

  def isLoggedUser = Reader[Environment, Boolean] { env =>
    env.authService.isLogged(env.userName)
  }
  def greetUser(logged: Boolean) = Reader[Environment, String] { env =>
    env.userService.greet(env.userName, logged)
  }

  val resultR: Reader[Environment, String] = for {
    logged <- isLoggedUser
    g <- greetUser(logged)
  } yield g

  val environment = Environment("Joe", new UserService, new AuthService)
  println(resultR.run(environment))

  /** type ReaderT[F[_], A, B] = Kleisli [F, A, B]
    * val ReaderT = Kleisli
    * type Reader[A, B] = Reader[Id, A, B]
    */

  /** type Writer[L, V] = WriterT[Id, L, V]
    * case class WriterT[F[_], L, V](run: F[(L, V)])
    */

  def greetW(name: String, logged: Boolean): Writer[List[String], String] =
    Writer(
      List("Composing a greeting"), {
        val userName = if (logged) name; else "User"
        s"HHHHHHello $userName"
      }
    )
  def isLoggedW(name: String): Writer[List[String], Boolean] = Writer(
    List("checking if user is loggedin"),
    name.length == 3
  )

  val name = "Xoe"
  val resultW: Writer[List[String], String] = for {
    logged <- isLoggedW(name)
    greeting <- greetW(name, logged)
  } yield (greeting)

  val (log, resultW2) = resultW.run
  println(log) //printsalllogmessages
  println(resultW2) // prints Hello Joe

  case class WWriter[V](run: (List[String], V)) {
    def bind[B](f: V => WWriter[B]): WWriter[B] = {
      val (log, value) = run
      val (nLog, nValue) = f(value).run
      WWriter((log ++ nLog, nValue))
    }
  }

  def genRandomChar: Char =
    (Random.nextInt(26) + 65).toChar
  println(List(genRandomChar, genRandomChar, genRandomChar).mkString)

  def getIntXorShift(seed: Int): Int = {
    var x = seed
    x ^= (x << 21)
    x ^= (x >>> 35)
    x ^= (x << 4)
    x
  }
  def getChar(seed: Int): (Int, Char) = {
    val newSeed = getIntXorShift(seed)
    val number = Math.abs(newSeed % 25) + 65
    (newSeed, number.toChar)
  }

  val nextCharS: State[Int, Char] = data.State[Int, Char] { seed =>
    getChar(seed)
  }

  val nextCharS2: State[Int, Char] =
    data.State[Int, Char](getChar)

  val randomS: State[Int, String] = for {
    f <- nextCharS2
    s <- nextCharS2
    t <- nextCharS2
  } yield (List(f, s, t).mkString)

  val initialSeed = 42
  val resultS: String = randomS.runA(initialSeed).value

  println(s" $resultS is the state state")

  /** Free monads
    */
  import cats.free._
  import cats.free.Free.liftF
  object freeT {
    trait ActionA[A]
    case class ReadAction() extends ActionA[String]
    case class WriteAction(output: String) extends ActionA[Unit]

    type ActionF[A] = Free[ActionA, A]

    def read(): ActionF[String] =
      liftF[ActionA, String](ReadAction())
    def write(o: String): ActionF[Unit] =
      liftF[ActionA, Unit](WriteAction(o))

    val resultFr: ActionF[Unit] = for {
      _ <- write("write your name")
      n <- read()
      u <- write(s"your name is $n")
    } yield (u)

    // import scala.io
    import cats.arrow.FunctionK
    import cats.Id

    val idInterpreter: FunctionK[ActionA, Id] = new FunctionK[ActionA, Id] {
      override def apply[A](fa: ActionA[A]): Id[A] = fa match {
        case ReadAction()           => StdIn.readLine()
        case WriteAction(o: String) => println(o)
      }
    }
  }
  import cats.data.Validated
  case class Person(name: String, email: String)
  val person = Person("Joe", "joe@eampe.com")

  def checkName(p: Person): Validated[String, String] =
    if (p.name.isEmpty)
      Validated.invalid("invalid name!!")
    else Validated.valid(p.name)

  def checkEmail(p: Person): Validated[String, String] =
    if (p.email.isEmpty)
      Validated.Invalid("invalid email!!")
    else Validated.Valid(p.email)
}
