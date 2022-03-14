package scala.service.api.v1.count

import cats.effect.IO
import com.typesafe.scalalogging.Logger

trait CountService:
  def count(string: String): IO[Either[Unit, Int]] 


object CountService extends CountService:

  def count(string: String): IO[Either[Unit, Int]] = for {
    length <- IO { Right(string.length) }
  } yield length

  private val logger: Logger = Logger[CountService]
