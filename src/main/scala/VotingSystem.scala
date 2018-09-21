import org.apache.spark.sql.SparkSession


object VotingSystem {


  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "VotingSystem"   --master local[4]   target/scala-2.11/voting-system_2.11-1.0.jar

  // Vote object used in ballotBox dataframe
  // case class Vote(voterId: Int, voteId: Int, candidate: String) {
  //   override def toString = s"$voterId $voteId $candidate"
  // }

  // Voter object used in voterList dataframe
  case class Voter(voterId: Int)

  val spark = SparkSession.builder.appName("Voting System").getOrCreate()
  import spark.sqlContext.implicits._

  // global variables, one set for both
  val voterTxt = spark.read.textFile("voterlist.txt")
  val ballotTxt = spark.read.textFile("ballotbox.txt")
  var validatedBallotBox = spark.read.textFile("ballotbox.txt").map(parseBallotBox).toDF()
  val testResult4 = "test_result4.txt"
  val testResult5 = "test_result5.txt"

  def parseVoterList(str: String): Voter = {
    val fields = str.split(" ")
    assert(fields.size == 1)
    Voter(fields(0).toInt)
  }

  def parseBallotBox(str: String): Vote = {
    val fields = str.split(",")
    assert(fields.size == 3)
    return new Vote(fields(0).substring(1, fields(0).length).toInt, fields(1).toInt, fields(2).substring(0, fields(2).length - 1))
  }

  def generateResults(): Unit = {
    spark.newSession()
    val ballotBox = ballotTxt.map(parseBallotBox)
    val vote_counts = ballotBox.groupBy("candidate").count()
    vote_counts.show()
    while (true) {}
    spark.stop()
  }

  def main(args: Array[String]) {
//    validateVote()
//    val vote = new Vote(1234, 120, "b")
//    castVote(vote)
    // removeDuplicates()
//    anonVote()
//    generateResults()
//    checkVote(211)
    import org.apache.spark.sql.DataFrame
    val ballotboxobj = new BallotBox()
    ballotboxobj.generateResults()


//    ballotboxobj.ballotBox = (DataFrame) (ballotTxt.map(parseBallotBox))
  }
}