import org.apache.spark.sql.SparkSession
object SimpleApp {

  // Vote object used in ballotBox dataframe
  case class Vote(voterId: Int, voteId: Int, candidate: String) {
    override def toString = s"$voterId $voteId $candidate"
  }

  // Voter object used in voterList dataframe
  case class Voter(voterId: Int)

  val sparkInital = SparkSession.builder.appName("Simple Application").getOrCreate()
  import sparkInital.sqlContext.implicits._

  // global variables, one set for both
  val voterTxt = sparkInital.read.textFile("voterlist.txt")
  val ballotTxt = sparkInital.read.textFile("ballotbox.txt")
  var validatedBallotBox = sparkInital.read.textFile("ballotbox.txt").map(parseBallotBox).toDF()
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
    Vote(fields(0).substring(1, fields(0).length).toInt, fields(1).toInt, fields(2).substring(0, fields(2).length - 1))
  }

  def validateVote() {//TODO make this drop unlegit voters -> updates validatedBallotBox
    //for every vote, call validateVote2
    //then, serialize validatedBallotBox to text file
    val rows = validatedBallotBox.collect()//.map(t => println(t)) //TODO: Do this using a filter
   
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val voterList = voterTxt.map(parseVoterList).toDF()

    for(i <- 0 until rows.length){
      val currVote = Vote(rows(i)(0).toString.toInt, rows(i)(1).toString.toInt, rows(i)(2).toString);
      val exists = voterList.filter($"voterId".contains(currVote.voterId)).count()
      voterList.show()
      if (exists == 1) {
      } else {
        printf("INVALID VOTER!!!!")
        //drop voter
        val id = currVote.voterId
        import org.apache.spark.sql.functions._
        validatedBallotBox = validatedBallotBox.filter(not(col("voterId") === lit(id)))
      }
    }
    validatedBallotBox.rdd.map(_.toString()).repartition(1).saveAsTextFile(testResult5)
  }



  def castVote(vote: Vote) = { //TODO make efficent
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotbox = ballotTxt.map(parseBallotBox).toDF()//TODO Remove this .toDF and find a different union method for the rdd
    val newRow = Seq((vote.voterId, vote.voteId, vote.candidate))
    val updatedBallotBox = ballotbox.union(newRow.toDF("voterId", "voteId", "candidate"))
    updatedBallotBox.show()
    while (true) {}
    spark.stop()
  }

  def removeDuplicates() {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = ballotTxt.map(parseBallotBox) //EDIT: removed .toDF()
    import org.apache.spark.sql.functions._
    val ballotBox2 = ballotBox.sort($"voteId".desc).groupBy("voterId").agg(first("voteId").as("voteId"), first("candidate").as("candidate")) //lazy evalution
    ballotBox2.rdd.map(_.toString()).repartition(1).saveAsTextFile(testResult4) //Evaluation actually happens here
    ballotBox2.show()
    while (true) {}
    spark.stop()
  }


  def anonVote(): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    import org.apache.spark.sql.functions._
    val ballotBox = ballotTxt.map(parseBallotBox)
    val updatedDf = ballotBox.withColumn("voterId", regexp_replace(col("voterId"), ".",null)) //TODO: Use a simpler function
    updatedDf.show() //Actually materialize and display the ballotbox
    while (true) {}
    spark.stop()
  }

  def generateResults(): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = ballotTxt.map(parseBallotBox)
    val vote_counts = ballotBox.groupBy("candidate").count() //Materializes and runs count operation
    vote_counts.show()
    while (true) {}
    spark.stop()
  }

  def checkVote(id: Int): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._
    val ballotBox = ballotTxt.map(parseBallotBox)
    val vote = ballotBox.filter($"voterId" === id)
    vote.show()
    while (true) {}
    spark.stop()
  }

  def main(args: Array[String]) {
//    validateVote()
//    val vote = new Vote(1234, 120, "b")
//    castVote(vote)
    removeDuplicates()
//    anonVote()
//    generateResults()
//    checkVote(211)
  }
}