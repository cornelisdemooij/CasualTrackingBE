package com.cornelisdemooij.casualtracking.persistence

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait Db {
  val config: DatabaseConfig[JdbcProfile]
  val db: JdbcProfile#Backend#Database = config.db
}
