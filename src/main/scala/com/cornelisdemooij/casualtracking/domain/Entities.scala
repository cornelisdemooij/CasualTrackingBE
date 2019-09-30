package com.cornelisdemooij.casualtracking.domain

import org.joda.time.DateTime

object Entities {
  // TODO Should be made into a generic class or a trait.
  case class DataPoint(id: Option[Long],
                       dataCollectionId: Long,
                       value: Double,
                       moment: DateTime,
                       note: Option[String])
  // TODO Should be made into a generic class or a trait.
  case class DataCollection(id: Option[Long],
                            name: String,
                            createdOn: DateTime)//,
                            //dataPointList: Option[List[DataPoint]])
}
