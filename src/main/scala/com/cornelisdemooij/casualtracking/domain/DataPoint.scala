package com.cornelisdemooij.casualtracking.domain

import akka.http.scaladsl.model.DateTime

// TODO Should be made into a generic class or a trait.
case class DataPoint(id: Option[Long],
                     value: Double,
                     moment: DateTime,
                     note: Option[String])
