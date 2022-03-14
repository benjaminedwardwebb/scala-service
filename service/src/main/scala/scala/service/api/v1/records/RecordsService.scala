package scala.service.api.v1.records

import cats.effect.IO
import com.typesafe.scalalogging.Logger
import java.util.UUID

class RecordsService(database: RecordsDatabase):

  def list(unit: Unit): IO[Either[Unit, Seq[Record]]] = for {
    records <- database.list
  } yield Right(records)

  def get(id: UUID): IO[Either[Unit, Record]] = for {
    record <- database.get(id)
    unitOrRecord = record.toRight(())
  } yield unitOrRecord

  def create(record: Record): IO[Either[Unit, Record]] = for {
    record <- database.create(record)
  } yield Right(record)

  private val logger: Logger = Logger[RecordsService]
