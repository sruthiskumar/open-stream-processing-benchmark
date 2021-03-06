package common.benchmark

import java.time.Instant
import java.util.Calendar
import java.util.concurrent.TimeUnit

import common.benchmark.input.Parsers
import org.scalatest.{Inside, Matchers, WordSpec}

class TestDataUtils extends WordSpec with Matchers with Inside {
  "The speed-parsing method" should {
    "parse a String correctly" in {
      val keyObservationToSplit = "u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00/lane1"
      val valueObservationToSplit = "{\"lat\":52.0265,\"long\":4.68309,\"speed\":48,\"accuracy\":95,\"num_lanes\":2,\"timestamp\":\"2017-03-02 14:38:00.000\"}"

      val calendar: Calendar = Calendar.getInstance()
      calendar.set(Calendar.YEAR, 2017)
      calendar.set(Calendar.MONTH, Calendar.MARCH)
      calendar.set(Calendar.DATE, 2)
      calendar.set(Calendar.HOUR_OF_DAY, 14)
      calendar.set(Calendar.MINUTE, 38)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)

      val (keyobservation, observation) = Parsers.parseLineSpeedObservation(keyObservationToSplit, valueObservationToSplit, Instant.now.toEpochMilli) //.get

      keyobservation should be(observation.measurementId + "/" + observation.internalId + "/" + observation.timestamp)

      inside(observation) {
        case speed: SpeedObservation =>
          speed.measurementId should be("u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00")
          speed.internalId should be("lane1")
          speed.timestamp should be(TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(calendar.getTime.getTime)))
          speed.latitude should be(52.0265)
          speed.longitude should be(4.68309)
          speed.speed should be(48)
          speed.accuracy should be(95)
          speed.numLanes should be(2)
      }
    }
  }

  "The flow-parsing method" should {
    "parse a String correctly" in {
      val keyObservationToSplit = "u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00/lane1"
      val valueObservationToSplit = "{\"lat\":52.0265,\"long\":4.68309,\"flow\":840,\"period\":60,\"accuracy\":95,\"num_lanes\":2,\"timestamp\":\"2017-03-02 14:38:00.0\"}"

      val calendar: Calendar = Calendar.getInstance()
      calendar.set(Calendar.YEAR, 2017)
      calendar.set(Calendar.MONTH, Calendar.MARCH)
      calendar.set(Calendar.DATE, 2)
      calendar.set(Calendar.HOUR_OF_DAY, 14)
      calendar.set(Calendar.MINUTE, 38)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)

      val (keyobservation, observation) = Parsers.parseLineFlowObservation(keyObservationToSplit, valueObservationToSplit, Instant.now.toEpochMilli) //.get

      keyobservation should be(observation.measurementId + "/" + observation.internalId + "/" + observation.timestamp)

      inside(observation) {
        case flow: FlowObservation =>
          flow.measurementId should be("u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00")
          flow.internalId should be("lane1")
          flow.timestamp should be(TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(calendar.getTime.getTime)))
          flow.latitude should be(52.0265)
          flow.longitude should be(4.68309)
          flow.flow should be(840)
          flow.period should be(60)
          flow.accuracy should be(95)
          flow.numLanes should be(2)
      }
    }
  }

  "The relative change calculation method" should {
    "parse a calculate the relative change correctly" in {
      //Current observation
      val newestObservation = AggregatableObservation(
        "u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00",
        List("lane1"),
        1488461880000l,
        52.0265,
        4.68309,
        840,
        60,
        95,
        48,
        95,
        2,
        1488461880000l,
        1488461880000l
      )

      //Observation 10 minutes ago
      val referenceObservation = Some(AggregatableObservation(
        "u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00",
        List("lane1"),
        1488461880000l - 10 * 60000,
        52.0265,
        4.68309,
        900,
        60,
        95,
        90,
        95,
        2,
        1488461880000l - 10 * 60000,
        1488461880000l - 10 * 60000
      ))

      inside(DataScienceMaths.calculateRelativeChangeBetweenObservations(newestObservation, None)) {
        case rel =>
          rel shouldBe None
      }

      inside(DataScienceMaths.calculateRelativeChangeBetweenObservations(newestObservation, referenceObservation)) {
        case rel =>
          rel shouldBe Some(RelativeChangePercentages((840d - 900d) / 900d, (48d - 90d) / 90d))
      }
    }
  }

  "extract timestamp method" should {
    "extract the timestamp of a flowobservation correctly" in {
      val calendar: Calendar = Calendar.getInstance()
      calendar.set(Calendar.YEAR, 2017)
      calendar.set(Calendar.MONTH, Calendar.MARCH)
      calendar.set(Calendar.DATE, 2)
      calendar.set(Calendar.HOUR_OF_DAY, 14)
      calendar.set(Calendar.MINUTE, 38)
      calendar.set(Calendar.SECOND, 4)
      calendar.set(Calendar.MILLISECOND, 33)

      val timestamp = Parsers.extractTimestamp("u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00/lane1 = {\"lat\":52.0265,\"long\":4.68309,\"flow\":840,\"period\":60,\"accuracy\":95,\"timestamp\":\"2017-03-02 14:38:15.000\"}")
      timestamp shouldBe TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(calendar.getTime.getTime))
    }

    "extract the timestamp of a speedobservation correctly" in {
      val calendar: Calendar = Calendar.getInstance()
      calendar.set(Calendar.YEAR, 2017)
      calendar.set(Calendar.MONTH, Calendar.MARCH)
      calendar.set(Calendar.DATE, 2)
      calendar.set(Calendar.HOUR_OF_DAY, 14)
      calendar.set(Calendar.MINUTE, 38)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)

      val timestamp = Parsers.extractTimestamp("u/1/5/r/f/x/4/h/7/f/s/c/PZH01_MST_0690_00/lane1 = {\"lat\":52.0265,\"long\":4.68309,\"speed\":48,\"accuracy\":95,\"timestamp\":\"2017-03-02 14:38:00.00\"}")
      timestamp shouldBe TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(calendar.getTime.getTime))
    }
  }

}
