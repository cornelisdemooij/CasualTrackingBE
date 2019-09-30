package com.cornelisdemooij.casualtracking.api

import com.cornelisdemooij.casualtracking.domain.Entities.DataPoint
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

trait CustomFormats {
  implicit object dateTimeFormat extends RootJsonFormat[DateTime] {
    private val parserISO: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
    override def write(obj: DateTime) = JsString(parserISO.print(obj))
    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parserISO.parseDateTime(s)
      case _ => throw DeserializationException("Error parsing string to date and time. Should be in ISO format.")
    }
  }
  implicit val dataPointFormat: RootJsonFormat[DataPoint] = jsonFormat5(DataPoint)
  implicit val dataPointListFormat: RootJsonFormat[List[DataPoint]] = jsonFormat1(List[DataPoint])
}
