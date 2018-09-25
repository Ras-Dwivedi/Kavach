import org.apache.spark.sql.SparkSession


class BallotBox extends java.io.Serializable{


  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "VotingSystem"   --master local[4]   target/scala-2.11/simple-project_2.11-1.0.jar
  import org.apache.spark.sql.DataFrame
  
  val spark = SparkSession.builder.appName("Voting System").getOrCreate()
  import spark.implicits._
  val ballotBox  = spark.read.textFile("ballotbox.txt").map(parseBallotBox)
  print(ballotBox)

  case class Voter(voterId: Int)

  import spark.sqlContext.implicits._

  // def validateVote() {//TODO make this drop unlegit voters -> updates validatedBallotBox
  //   //for every vote, call validateVote2
  //   //then, serialize validatedBallotBox to text file
  //   val rows = validatedBallotBox.collect()//.map(t => println(t)) //TODO: Do this using a filter
   
  //   val spark = SparkSession.builder.appName("Voting System").getOrCreate()
  //   val sqlContext = spark.sqlContext
  //   import sqlContext.implicits._
  //   val voterList = voterTxt.map(parseVoterList).toDF()

  //   for(i <- 0 until rows.length){
  //     val currVote = Vote(rows(i)(0).toString.toInt, rows(i)(1).toString.toInt, rows(i)(2).toString);
  //     val exists = voterList.filter($"voterId".contains(currVote.voterId)).count()
  //     voterList.show()
  //     if (exists == 1) {
  //     } else {
  //       printf("INVALID VOTER!!!!")
  //       //drop voter
  //       val id = currVote.voterId
  //       import org.apache.spark.sql.functions._
  //       validatedBallotBox = validatedBallotBox.filter(not(col("voterId") === lit(id)))
  //     }
  //   }
  //   validatedBallotBox.rdd.map(_.toString()).repartition(1).saveAsTextFile(testResult5)
  // }



  // def castVote(vote: Vote) = {
  //   spark.newSession()
  //   // val ballotbox = ballotTxt.map(parseBallotBox).toDF()
  //   val updatedBallotBox = ballotBox.union(Seq((vote.voterId, vote.voteId, vote.candidate)).toDF())
  //   updatedBallotBox.show()
  //   while (true) {}
  //   spark.stop()
  // }

  def parseBallotBox(str: String): Vote = {
    val fields = str.split(",")
    assert(fields.size == 3)
    Vote(fields(0).substring(1, fields(0).length).toInt, fields(1).toInt, fields(2).substring(0, fields(2).length - 1))
  }

  def removeDuplicates() {
    spark.newSession()

    import org.apache.spark.sql.functions._
    import spark.implicits._
    val ballotBox2 = ballotBox.sort($"voteId".desc)
                    .groupBy("voterId")
                    .agg(first("voteId").as("voteId"), first("candidate").as("candidate"))
    print("BALLOTBOX2!!!!!!!!!!!!")
    print(ballotBox2)
    print(ballotBox2.getClass())
    val ballotBox3 = ballotBox2.count()
    print(ballotBox3)
//    ballotBox2.collect().foreach(println)
    while (true) {}
    spark.stop()
  }


  def anonVote(): Unit = {
    spark.newSession()
    import spark.sqlContext.implicits._
    import org.apache.spark.sql.functions._
    val updatedDf = ballotBox.withColumn("voterId", regexp_replace(col("voterId"), ".",null))
    while (true) {}
    spark.stop()
  }

  def generateResults(): Unit = {
    spark.newSession()
      // import spark.sqlContext.implicits._

    // val ballotBox = ballotTxt.map(parseBallotBox)
    import spark.implicits._

    val vote_counts = ballotBox.groupBy("candidate").count()
    vote_counts.show()
    while (true) {}
    spark.stop()
  }

  def checkVote(id: Int): Unit = {
    spark.newSession()
    import spark.implicits._
    val vote = ballotBox.filter($"voterId" === id)
    vote.show()
    while (true) {}
    spark.stop()
  }




}