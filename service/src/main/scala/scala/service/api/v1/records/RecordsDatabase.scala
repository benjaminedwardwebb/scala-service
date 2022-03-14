package scala.service.api.v1.records

import cats.effect.{IO, Resource}
import com.typesafe.scalalogging.Logger
import doobie.LogHandler
import doobie.h2.H2Transactor
import doobie.syntax.all.{toSqlInterpolator, toConnectionIOOps}
import doobie.{ConnectionIO, ExecutionContexts, Meta}
import java.util.UUID

trait RecordsDatabase:
  def list: IO[Seq[Record]]
  def get(id: UUID): IO[Option[Record]]
  def create(record: Record): IO[Record]


object RecordsDatabase extends RecordsDatabase:

  def initialize: IO[RecordsDatabase] = for {
    _ <- IO.blocking({ logger.debug("Dropping records table if it exists and then creating it.") })
    _ <- transactor.use({ xa =>
      dropTableIfExistsAndThenCreateTable.transact(xa)
    })
    _ <- IO.blocking({ logger.debug("Dropped and created records table.") })
    _ <- IO.blocking({ logger.info("Initialized RecordsDatabase.") })
    database = this
  } yield database

  def list: IO[Seq[Record]] = transactor.use { xa =>
    selectAll.transact(xa)
  }

  def get(id: UUID): IO[Option[Record]] = transactor.use { xa =>
    selectById(id).transact(xa)
  }

  def create(record: Record): IO[Record] = transactor.use { xa =>
    insert(record).transact(xa)
  }

  private val logger: Logger = Logger[RecordsDatabase]

  private implicit val logHandler: LogHandler = LogHandler.jdkLogHandler

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  private val transactor: Resource[IO, H2Transactor[IO]] = {
    for {
      executionContexts <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      xa <- H2Transactor.newH2Transactor[IO](
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
        "sa",                           // username
        "",                           // password
        executionContexts,                    // await connection here
      )
    } yield xa
  }

  private implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap[UUID](UUID.fromString)(_.toString)

  private def dropTableIfExistsAndThenCreateTable: ConnectionIO[Unit] = for {
    drop <- sql"""DROP TABLE IF EXISTS records""".update.run
    create <- sql"""
      CREATE TABLE records (
        id  UUID,
        x   VARCHAR NOT NULL,
        y   INT NOT NULL,
        z   BOOLEAN NOT NULL
      )
    """.update.run
  } yield ()

  private def selectAll: ConnectionIO[Seq[Record]] = for {
    records <- sql"select * from records".query[Record].to[Seq]
  } yield records

  private def selectById(id: UUID): ConnectionIO[Option[Record]] = for {
    record <- sql"select * from records where id = $id".query[Record].option
  } yield record

  private def insert(record: Record): ConnectionIO[Record] = for {
    id <- sql"""
      insert into records (id, x, y, z)
      values (${record.id}, ${record.x}, ${record.y}, ${record.z})
    """.update.withUniqueGeneratedKeys[UUID]("id")
    record <- sql"select * from records where id = $id".query[Record].unique
  } yield record
