import org.apache.spark.sql.SparkSession


class BallotBox extends java.io.Serializable {
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "VotingSystem"   --master local[4]   target/scala-2.11/voting-system_2.11-1.0.jar

  val spark = SparkSession.builder.appName("Voting System").getOrCreate()
  import spark.implicits._
  val ballotBox  = spark.read.textFile("ballotbox.txt").map(parseBallotBox).as[Vote]
  case class Voter(voterId: Int)
  import spark.sqlContext.implicits._

  def parseBallotBox(str: String): Vote = {
    val fields = str.split(",")
    assert(fields.size == 3)
    Vote(fields(0).substring(1, fields(0).length).toInt, fields(1).toInt, fields(2).substring(0, fields(2).length - 1))
  }

  def addVote(vote: Vote) : Any = {
      spark.newSession()
      print(ballotBox.getClass())
      import spark.sqlContext.implicits._
      val ds = Seq(Vote(vote.readCredential(), vote.readVoteId(), vote.readVote())).toDS()
      val updatedBallotBox = ballotBox.union(ds)
      updatedBallotBox.show()
      return updatedBallotBox
      spark.stop()
   }

  def removeDuplicates() : Any = {
    spark.newSession()
    import org.apache.spark.sql.functions._
    import spark.implicits._
    val ballotBox2 = ballotBox.map(v => (v.readCredential(), v)).groupByKey(_._1).reduceGroups((a, b) => (a._1, b._2)).map(_._2)
    val ballotBox3 = ballotBox2.select(col("_2")).as("Vote")
    ballotBox3.show()
    return ballotBox2
    spark.stop()
  }

  def generateResults(): Any = {
    spark.newSession()
    import spark.implicits._
    val vote_counts = ballotBox.map(v => (v.readVote(), 1)).groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 + b._2)).map(_._2)
    vote_counts.show()
    return vote_counts
    spark.stop()
  }

  def checkVote(id: Int): Any = {
    spark.newSession()
    import spark.implicits._
    val filtered_ballotbox = ballotBox.filter(v => v.readCredential() == id)
    filtered_ballotbox.show()
    return filtered_ballotbox
    spark.stop()
  }
}
