import org.apache.spark.sql.SparkSession

object SimpleApp {
  case class Vote(voterId: Int, voteId: Int, candidate: String) {
    override def toString = s"$voterId $voteId $candidate"
  }
  case class Voter(voterId: Int)

  def parseVoterList(str: String): Voter = {
    val fields = str.split(" ")
    assert(fields.size == 1)
    Voter(fields(0).toInt)
  }

  def parseBallotBox(str: String): Vote = {
    val fields = str.split(" ")
    assert(fields.size == 3)
    Vote(fields(0).toInt, fields(1).toInt, fields(2))
  }

  def castVote(vote: Vote): Boolean = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val voterList = spark.read.textFile("/Users/sumukhshivakumar/Desktop/voterList.txt").map(parseVoterList).toDF()
    val exists = voterList.filter($"voterId".contains(vote.voterId)).count()
    voterList.show()
    if (exists == 1) {
      val ballotbox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
      val df = Seq((vote.voterId, vote.voteId, vote.candidate)).toDF("voterId", "voteId", "candidate")
      val updatedBallotBox = ballotbox.union(df)
      updatedBallotBox.show()
      spark.stop()
      true
    } else {
      printf("INVALID VOTER!!!!")
      spark.stop()
      false
    }
  }

  def removeDuplicates() {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    import org.apache.spark.sql.functions._
    val ballotBox2 = ballotBox.sort($"voteId".desc).groupBy("voterId").agg(first("voterId").as("voterId"), first("voteId").as("voteId"), first("candidate").as("candidate"))
    ballotBox2.rdd.map(_.toString()).repartition(1).saveAsTextFile("/Users/sumukhshivakumar/Desktop/test_result4.txt")
    ballotBox2.show()
    spark.stop()
  }


  def anonVote(): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    import org.apache.spark.sql.functions._
    val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    val updatedDf = ballotBox.withColumn("voterId", regexp_replace(col("voterId"), ".",null))
    updatedDf.show()
    spark.stop()
  }

  def generateResults(): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    ballotBox.show()
    val vote_counts = ballotBox.groupBy("candidate").count()
    vote_counts.show()
    //    vote_counts.write.format("csv").save("/Users/sumukhshivakumar/Desktop/vote_counts.csv")
    spark.stop()
  }

  def checkVote(id: Int): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    ballotBox.show()

  }



  def main(args: Array[String]) {
    //    count_votes()
    //    anon_vote()
//    val vote = new Vote(1234, 120, "b")
//    castVote(vote)
    removeDuplicates()
  }

}