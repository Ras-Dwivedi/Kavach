case class Vote(voterId: Int, voteId: Int, candidate: String) {

  override def toString = s"$voterId $voteId $candidate\n"

  var policy = new VotePolicy(new VotingState("Voting"))

  def printState() = {
      println(policy.policyState.name)
  }

  def compareCredential(vote: Vote): Boolean = {
    return vote.readCredential() == readCredential()
  }

  def readCredential(): Int = {
    policy.transition(new ReadCredential("readCredential"))
    return voterId
  }

  def readVoteId(): Int = {
    return voteId
  }

  def readVote(): String = {
    policy.transition(new ReadVote("readVote"))
    return candidate
  }

  def anonVote(): Vote = {
    policy.transition(new AnonVote("anonVote"))
    return Vote(null.asInstanceOf[Int], voterId, candidate)
  }
}
