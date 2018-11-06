import org.apache.spark.sql.SparkSession

object Session {
  val spark: SparkSession = SparkSession.builder.appName("Voting System").getOrCreate()
}

object VotingSystem {
  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "VotingSystem"   --master local[4]   target/scala-2.11/voting-system_2.11-1.0.jar
  def parseBallotBox(str: String): Vote = {
    val fields = str.split(",")
    assert(fields.size == 3)
    Vote(fields(0).substring(1, fields(0).length).toInt, fields(1).toInt, fields(2).substring(0, fields(2).length - 1))
  }

//   def main(args: Array[String]) {
//     import Session.spark.implicits._
//     val ballotBoxDS  = Session.spark.read.textFile("ballotbox.txt").map(parseBallotBox).as[Vote]
//     val ballotboxobj = new BallotBox(ballotBoxDS)

//     Session.spark.newSession()
//     import Session.spark.implicits._
//     // ballotboxobj.removeDuplicates()
//     //    ballotboxobj.addVote(vote)
//     //    ballotboxobj.anonVote()
//         ballotboxobj.generateResults()
//     //    ballotboxobj.checkVote(211)
//     Session.spark.stop()
// //    val vote = new Vote(1234, 120, "b")
// //    println("Original State")
// //    vote.printState()
// //    println("State After readVote")
// //    vote.readVote()
// //    vote.printState()
// //    println("State After anonVote")
// //    vote.anonVote()
// //    vote.printState()
//   }
}