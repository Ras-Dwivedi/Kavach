import org.apache.spark.sql.SparkSession
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object SessionHealthCare {
  val spark = SparkSession.builder.appName("Voting System").getOrCreate()
}

object HealthCareSystem {
  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "HealthCareSystem"   --master local[4]   target/scala-2.11/health-care-system_2.11-1.0.jar

  def ageGroup(age: String): String = {
    val ageInt = age.toInt
    if (ageInt < 10) {
      return "0-10"
    }
    else if (ageInt < 20) {
      return "10-20"
    }
    else if (ageInt < 30) {
      return "20-30"
    }
    else if (ageInt < 40) {
      return "30-40"
    }
    else if (ageInt < 50) {
      return "40-50"
    }
    else if (ageInt < 60) {
      return "50-60"
    }
    else if (ageInt < 70) {
      return "60-70"
    }
    else if (ageInt < 80) {
      return "70-80"
    }
    else {
      return "80+"
    }
  }

  def calculateAge(born: String): Int = {
    val fields = born.split(",")
    val format = new java.text.SimpleDateFormat("MM-dd-yyyy")
    val today = format.format(Calendar.getInstance().getTime()).toString()

    val startDate = born
    val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
    val oldDate = LocalDate.parse(startDate, formatter)
    val currentDate = today
    val newDate = LocalDate.parse(currentDate, formatter)
    return (newDate.toEpochDay() - oldDate.toEpochDay()).toInt
  }

//  def prepareData(str: String): SimpleDateFormat = {
//    val format = new java.text.SimpleDateFormat("MM-dd-yyyy")
//    try {
//      val born = format.parse(str)
//    }
//    return calculateAge(born)
//  }

  def patientAttributes(str: String): Patient = {
    // val fields = str.split(",")
     val cols = str.split(",")
    // println(Patient(cols(0).toInt, cols(1), cols(2), cols(3), cols(4), cols(5), cols(6).toInt, cols(7)))
    return Patient(cols(0).toInt, cols(1), cols(2), cols(3), cols(4), cols(5), cols(6).toInt, cols(7))
  }

  def main(args: Array[String]) {
    import Session.spark.implicits._

    var patientFileInitial  = Session.spark.read.textFile("patients.csv")
    println("Patient count: " + patientFileInitial.count())

    // SCENARIO 1
    val patient_demographics = patientFileInitial.map(patientAttributes).as[Patient]

    // SCENARIO 2
    val patientFile  = Session.spark.read.textFile("patients.csv").map(patientAttributes)

    // 1. Find the distribution of male and female patients
    val patientGender = patientFile.map(v => (v.readGender(), 1)).groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 + b._2)).map(_._2)
    println("Gender types: " + patientGender.count())
    patientGender.show()
    

    // 2. Find distribution for married status
    val patientMarriedStatus = patientFile.map(v => (v.readMartialStatus(), 1)).groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 + b._2)).map(_._2)
    println("Martial Status types: " + patientMarriedStatus.count())
    patientMarriedStatus.show()

    // 3. Find distribution for different age groups
    val patientAgeGroupWise = patientFile.map(v => (v.readAgeGroup(), 1)).groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 + b._2)).map(_._2)
    println("Age group types: " + patientAgeGroupWise.count())
    patientAgeGroupWise.show()

    // 4. Find top 5 cities from where we have most number of patients with patient frequency
    import org.apache.spark.sql.functions.countDistinct
    val patientCityWise = patientFile.toDF().agg(countDistinct($"city")).head(5)
    println("Number of distinct cities: ")
    patientCityWise.foreach(println)

    // 5. Find distribution smoking_status/smoking habit
    val patientSmokingWise = patientFile.map(v => (v.readSmokingStatus(), 1)).groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 + b._2)).map(_._2)
    patientSmokingWise.show()
  }
}
