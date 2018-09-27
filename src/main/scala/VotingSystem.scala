import org.apache.spark.sql.SparkSession


object VotingSystem {


  //Command needed to run
  //export SPARK_HOME=/usr/local/Cellar/apache-spark/2.3.1/libexec
  //$SPARK_HOME/bin/spark-submit   --class "VotingSystem"   --master local[4]   target/scala-2.11/voting-system_2.11-1.0.jar

  def main(args: Array[String]) {
    import org.apache.spark.sql.DataFrame
    val ballotboxobj = new BallotBox()
    val vote = new Vote(1234, 120, "Ankush D.")
    val vote2 = new Vote(1234, 121, "Shiv K.")
//    print(ballotboxobj.generateResults())
    print("HELLOOOOOO\n")
//    print(ballotboxobj.checkVote(211))
//    print(ballotboxobj.generateResults())
//    print(ballotboxobj.removeDuplicates())

//    print(vote.compareCredential(vote2))
//    print(vote.readCredential())
//    print(vote.readVote())
//    print(vote.anonVote())

//    ballotboxobj.removeDuplicates()
    ballotboxobj.addVote(vote)
//    ballotboxobj.anonVote()
//    ballotboxobj.generateResults()
//    ballotboxobj.checkVote(211);
//    ballotboxobj.ballotBox = (DataFrame) (ballotTxt.map(parseBallotBox))
  }
}