import org.apache.spark.sql.SparkSession


object VotingSystem {


  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "VotingSystem"   --master local[4]   target/scala-2.11/voting-system_2.11-1.0.jar

  def main(args: Array[String]) {
    //    validateVote()
        val vote = new Vote(1234, 120, "b")
    //    castVote(vote)
    // removeDuplicates()
    //    anonVote()
    //    generateResults()
    //    checkVote(211)
    import org.apache.spark.sql.DataFrame
    val ballotboxobj = new BallotBox()
    ballotboxobj.anonVote()
//    ballotboxobj.generateResults()
//    ballotboxobj.removeDuplicates()
//      ballotboxobj.checkVote(211);


    //    ballotboxobj.ballotBox = (DataFrame) (ballotTxt.map(parseBallotBox))
  }
}