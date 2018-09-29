case class Vote(voterId: Int, voteId: Int, candidate: String) {

  override def toString = s"$voterId $voteId $candidate\n"

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
