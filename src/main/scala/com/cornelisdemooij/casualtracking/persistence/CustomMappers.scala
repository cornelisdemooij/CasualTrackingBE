package com.cornelisdemooij.casualtracking.persistence

import java.sql.Timestamp

import org.joda.time.DateTime

trait CustomMappers { this: Db =>
  import config.profile.api._

  implicit def dateTimeMapper: BaseColumnType[DateTime] =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime)
    )
}
