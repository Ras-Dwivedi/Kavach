case class Vote(voterId: Int, voteId: Int, candidate: String) {

  override def toString = s"$voterId $voteId $candidate\n"

  var policy = new VotePolicy(VotePolicyState.Voting)

  def printPolicy(): Any = {
      println(policy)
      return
  }

  def printState(): Any = {
      println(policy.policyState)
      return
  }

  def compareCredential(vote: Vote): Boolean = {
    return vote.readCredential() == readCredential()
  }

  def readCredential(): Int = {
    policy.transition(ReadCredential())
    return voterId
  }

  def readVoteId(): Int = {
    return voteId
  }

  def readVote(): String = {
    policy.transition(ReadVote())
    return candidate
  }

  def anonVote(): Vote = {
    policy.transition(AnonVote())
    return Vote(null.asInstanceOf[Int], voterId, candidate)
  }
}
