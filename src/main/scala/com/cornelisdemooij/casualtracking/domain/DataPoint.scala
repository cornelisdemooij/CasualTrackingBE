package com.cornelisdemooij.casualtracking.domain

import org.joda.time.DateTime

// TODO Should be made into a generic class or a trait.
case class DataPoint(id: Option[Long],
                     value: Double,
                     moment: DateTime,
                     note: Option[String])
