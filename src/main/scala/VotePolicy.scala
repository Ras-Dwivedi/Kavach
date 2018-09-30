// case class VotePolicy(policyState: VotePolicyState.State)

class VotePolicy(var policyState: VotePolicyState.State) extends Policy{

  private def changeState(s: VotePolicyState.State) : Any = {
    policyState = s
    return
    // d.copy(policyState = s)
  }

  def transition(e: Event): Any = {
    policyState match {
      case VotePolicyState.Voting =>
        e match {
          case ReadVote() => changeState(VotePolicyState.Voting)
          case ReadCredential() => changeState(VotePolicyState.Voting)
          case AnonVote() => changeState(VotePolicyState.VotingComplete)
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case VotePolicyState.Voting =>
        e match {
          case ReadVote() => changeState(VotePolicyState.VotingComplete)
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case _ => throw new IllegalArgumentException("Illegal Operation");
    }
  }

}