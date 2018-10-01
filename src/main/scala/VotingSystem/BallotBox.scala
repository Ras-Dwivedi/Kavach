import org.apache.spark.sql.SparkSession
import org.apache.spark.sql
import org.apache.spark.sql.Dataset

class BallotBox (bb: Dataset[Vote]) extends java.io.Serializable {
  var policy = new BallotBoxPolicy(BB_VotingState("Voting"))

  val ballotBox : Dataset[Vote] = bb
  case class Voter(voterId: Int)

  def addVote(vote: Vote) : Any = {
      policy.transition(CastVote("addVote"))

      print(ballotBox.getClass())
      import Session.spark.sqlContext.implicits._
      val ds = Seq(Vote(vote.readCredential(), vote.readVoteId(), vote.readVote())).toDS()
      val updatedBallotBox = ballotBox.union(ds)
      updatedBallotBox.show()
      return updatedBallotBox
   }

  def removeDuplicates() : Any = {
    policy.transition(RemoveDuplicate("removeDuplicates"))

    import org.apache.spark.sql.functions._
    import Session.spark.implicits._
    val ballotBox2 = ballotBox.map(v => (v.readCredential(), v)).groupByKey(_._1).reduceGroups((a, b) => (a._1, b._2)).map(_._2)
    val ballotBox3 = ballotBox2.select(col("_2")).as("Vote")
    ballotBox3.show()
    return ballotBox2
  }

  def generateResults(): Any = {
    policy.transition(CountVotes("generateResults"))

    import Session.spark.implicits._
    val vote_counts = ballotBox.map(v => (v.readVote(), 1)).groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 + b._2)).map(_._2)
    vote_counts.show()
    return vote_counts
  }

  def checkVote(id: Int): Any = {
    policy.transition(CheckVote("checkVote"))

    import Session.spark.implicits._
    val filtered_ballotbox = ballotBox.filter(v => v.readCredential() == id)
    filtered_ballotbox.show()
    return filtered_ballotbox
  }
}
