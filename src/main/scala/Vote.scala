case class Vote(voterId: Int, voteId: Int, candidate: String) {

  override def toString = s"$voterId $voteId $candidate\n"

  var policy = new VotePolicy("test", VotePolicyState.State1)

  def readVote2(): VotePolicy = {
    policy = VotePolicy.transition(policy, Action2("hello"))
    return policy
  }

  def printPolicy(): Any = {
      println(policy)
      return
  }

  def compareCredential(vote: Vote): Boolean = {
    return vote.readCredential() == readCredential()
  }

  def readCredential(): Int = {
    return voterId
  }

  def readVoteId(): Int = {
    return voteId
  }

  def readVote(): String = {
    return candidate
  }

  def anonVote(): Vote = {
    return Vote(null.asInstanceOf[Int], voterId, candidate)
  }
}
