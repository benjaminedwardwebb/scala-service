package scala.service.api.doobie

import cats.effect.IO
import cats.effect._
import cats.implicits._
import doobie.ExecutionContexts
import doobie.h2.H2Transactor
import doobie.implicits._
import doobie._
import doobie.h2._
import java.util.UUID
import scala.concurrent.ExecutionContext
import doobie.LogHandler

object DoobieService:

  def list(unit: Unit): IO[Either[Unit, Seq[Record]]] = for {
    records <- DoobieDatabase.list
  } yield Right(records)

  def get(id: UUID): IO[Either[Unit, Record]] = for {
    record <- DoobieDatabase.get(id)
    a = record.toRight(())
  } yield a

  def post(record: Record): IO[Either[Unit, Record]] = for {
    record <- DoobieDatabase.post(record)
  } yield Right(record)
