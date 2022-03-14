package scala.service.api.v1.hello

import cats.effect.IO
import com.typesafe.scalalogging.Logger

trait HelloService:
  def hello(unit: Unit): IO[Either[Unit, String]] 


object HelloService extends HelloService:

  def hello(unit: Unit): IO[Either[Unit, String]] = for {
    string <- IO { Right("hello, world!") }
  } yield string

  private val logger: Logger = Logger[HelloService]
