import org.apache.spark.sql.SparkSession
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Session {
  val spark = SparkSession.builder.appName("Voting System").getOrCreate()
}

object VotingSystem {
  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "HealthCareSystem"   --master local[4]   target/scala-2.11/health-care-system_2.11-1.0.jar

  def ageGroup(age: String): String = {
    if (age < 10) {
      return ‘0-10’
    }
    else if (age < 20) {
      return ’10-20′
    }
    else if (age < 30) {
      return ’20-30′
    }
    else if (age < 40) {
      return ’30-40′
    }
    else if (age < 50) {
      return ’40-50′
    }
    else if (age < 60) {
      return ’50-60′
    }
    else if (age < 70) {
      return ’60-70′
    }
    else if (age < 80) {
      return ’70-80′
    }
    else {
      return ’80+’
    }
  }

  def calculateAge(born: String): Int = {
    val fields = str.split(",")
    val format = new java.text.SimpleDateFormat("MM-dd-yyyy")
    val today = format.format(Calendar.getInstance().getTime()).toString()

    val startDate = born
    val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
    val oldDate = LocalDate.parse(startDate, formatter)
    val currentDate = today
    val newDate = LocalDate.parse(currentDate, formatter)
    return (newDate.toEpochDay() - oldDate.toEpochDay()).toInt()
  }

//  def prepareData(str: String): SimpleDateFormat = {
//    val format = new java.text.SimpleDateFormat("MM-dd-yyyy")
//    try {
//      val born = format.parse(str)
//    }
//    return calculateAge(born)
//  }

  def patientAttributes(str: String): Patient = {
    val fields = str.split(",")
    return Patient(fields(0).substring(0, fields(0).length).toInt, fields(1).substring(0, fields(1).length), fields(2).substring(0, fields(2).length), fields(3).substring(0, fields(3).length), fields(4).substring(0, fields(4).length), fields(5).substring(0, fields(5).length), calculateAge(fields(1).substring(0, fields(1).length)), ageGroup(fields(1).substring(0, fields(1).length)))
  }

  def main(args: Array[String]) {
    import Session.spark.implicits._
    val patientFile  = Session.spark.read.textFile("patients.csv")
    patientFile.count()

    val patient_demographics = patientFile.filter(v => !(v._1 contains "patient_id")).map(v => patientAttributes(v))
//    val ballotboxobj = new BallotBox(ballotBoxDS)

//    Session.spark.newSession()
//    import Session.spark.implicits._
//    ballotboxobj.generateResults()
//    Session.spark.stop()
  }
}
