case class Vote(voterId: Int, voteId: Int, candidate: String) {

  override def toString = s"$voterId $voteId $candidate\n"

  var policy = new VotePolicy(V_VotingState("Voting"))

  def printState() = {
      println("Vote policy in state: " + policy.policyState.name)
  }

  def readCredential(): Int = {
    policy.transition(ReadCredential("readCredential"))
    printState()
    return voterId
  }

  def readVoteId(): Int = {
    policy.transition(ReadVoteId("readVoteId"))
    printState()
    return voteId
  }

  def readVote(): String = {
    policy.transition(ReadVote("readVote"))
    printState()
    return candidate
  }

  def anonVote(): Vote = {
    policy.transition(AnonVote("anonVote"))
    printState()
    return Vote(null.asInstanceOf[Int], voterId, candidate)
  }
}
