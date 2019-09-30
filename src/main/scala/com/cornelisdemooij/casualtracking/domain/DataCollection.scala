package com.cornelisdemooij.casualtracking.domain

import org.joda.time.DateTime

// TODO Should be made into a generic class or a trait.
case class DataCollection(id: Option[Long],
                          name: String,
                          createdOn: DateTime,
                          dataPointList: Option[List[DataPoint]])
