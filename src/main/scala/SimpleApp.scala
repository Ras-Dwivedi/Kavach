import org.apache.spark.sql.SparkSession
//TODO: Visualization of DAG
object SimpleApp {

  //$SPARK_HOME/bin/spark-submit   --class "SimpleApp"   --master local[4]   target/scala-2.11/simple-project_2.11-1.0.jar


  case class Vote(voterId: Int, voteId: Int, candidate: String) {
    override def toString = s"$voterId $voteId $candidate"
  }
  case class Voter(voterId: Int)

  val sparkInital = SparkSession.builder.appName("Simple Application").getOrCreate()
  // val voterTxt = sparkInital.read.textFile("/Users/sumukhshivakumar/Desktop/voterlist.txt")
  // val ballotTxt = sparkInital.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt")
  val voterTxt = sparkInital.read.textFile("/Users/Shiv/Desktop/voterList.txt")
  val ballotTxt = sparkInital.read.textFile("/Users/Shiv/Desktop/ballotbox.txt")
  import sparkInital.sqlContext.implicits._
  // var validatedBallotBot = sparkInital.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
  var validatedBallotBot = sparkInital.read.textFile("/Users/Shiv/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
  //sparkInital.stop()



  def parseVoterList(str: String): Voter = {
    val fields = str.split(" ")
//    val fields = str.split(",")
    assert(fields.size == 1)
    Voter(fields(0).toInt)
  }

  def parseBallotBox(str: String): Vote = {
    val fields = str.split(",")
    assert(fields.size == 3)
    // Vote(fields(0).toInt, fields(1).toInt, fields(2))
   Vote(fields(0).substring(1, fields(0).length).toInt, fields(1).toInt, fields(2).substring(0, fields(2).length - 1))
  }

  def validateVote() {//TODO make this drop unlegit voters -> updates validatedBallotBot
    //for every vote, call validateVote2
    //then, serialize validatedBallotBot to text file
    val rows = validatedBallotBot.collect()//.map(t => println(t)) //TODO: Do this using a filter
    // for ((a, b, c) <- rows) {
    //   val currVote = Vote(a, b, c)
    // }
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val voterList = voterTxt.map(parseVoterList).toDF()

    for(i <- 0 until rows.length){

      val currVote = Vote(rows(i)(0).toString.toInt, rows(i)(1).toString.toInt, rows(i)(2).toString);
      //validateVote2(currVote)
      val exists = voterList.filter($"voterId".contains(currVote.voterId)).count()
      voterList.show()
      if (exists == 1) {
        
      } else {
        printf("INVALID VOTER!!!!")
        //drop voter
        // validatedBallotBot = validatedBallotBot.filter(not($"voterId" == vote.voterId))
        val id = currVote.voterId
        // validatedBallotBot = validatedBallotBot.filter("voterId == id")
        import org.apache.spark.sql.functions._

        validatedBallotBot = validatedBallotBot.filter(not(col("voterId") === lit(id)))
        // validatedBallotBot = validatedBallotBot.filter(col("voterId") < lit(id))



        //spark.stop()
        
      }


    }

    validatedBallotBot.rdd.map(_.toString()).repartition(1).saveAsTextFile("/Users/Shiv/Desktop/test_result5.txt")

    // rows.foreach {
    //   val currVote = Vote(_(0), _(1), _(2))
    //   validateVote2(currVote)
    // }




  }



  def castVote(vote: Vote) = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val voterList = voterTxt.map(parseVoterList).toDF() //Delete line //TODO find if .toDF is concertizing the voeterTxt
    // val voterList = spark.read.textFile("/Users/sumukhshivakumar/Desktop/voterList.txt").map(parseVoterList).toDF()
    val ballotbox = ballotTxt.map(parseBallotBox)
    // val ballotbox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    val df = Seq((vote.voterId, vote.voteId, vote.candidate))
    val updatedBallotBox = ballotbox.union(df)
    updatedBallotBox.show()
    spark.stop()

  }

  def removeDuplicates() {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = ballotTxt.map(parseBallotBox).toDF()
    // val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    import org.apache.spark.sql.functions._
    val ballotBox2 = ballotBox.sort($"voteId".desc).groupBy("voterId").agg(first("voteId").as("voteId"), first("candidate").as("candidate"))
    ballotBox2.rdd.map(_.toString()).repartition(1).saveAsTextFile("/Users/Shiv/Desktop/test_result4.txt")
    // ballotBox2.rdd.map(_.toString()).repartition(1).saveAsTextFile("/Users/sumukhshivakumar/Desktop/test_result4.txt")
    ballotBox2.show()
    spark.stop()
  }


  def anonVote(): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    import org.apache.spark.sql.functions._
    val ballotBox = ballotTxt.map(parseBallotBox).toDF()
    // val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
    val updatedDf = ballotBox.withColumn("voterId", regexp_replace(col("voterId"), ".",null))
    updatedDf.show()
    spark.stop()
  }

  def generateResults(): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = ballotTxt.map(parseBallotBox).toDF()
    // val ballotBox = spark.read.textFile("/Users/sumukhshivakumar/Desktop/ballotbox.txt").map(parseBallotBox).toDF()
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
    val ballotBox = ballotTxt.map(parseBallotBox).toDF()
    ballotBox.show()
    val vote = ballotBox.filter($"voterId" === id)
    vote.show()

  }

  def main(args: Array[String]) {
    //    count_votes()
    //    anon_vote()
    //    val vote = new Vote(1234, 120, "b")
    //    castVote(vote)
    //    removeDuplicates()
    //checkVote(211)
    validateVote()
  }

}