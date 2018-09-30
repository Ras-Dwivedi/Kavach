case class VotePolicy(name: String, policyState: VotePolicyState.State)

object VotePolicy extends Policy{

  private def cloneNewState(d: VotePolicy, s: VotePolicyState.State) =
    d.copy(policyState = s)

  def transition(d: VotePolicy, e: Event): VotePolicy = {
    d.policyState match {
      case VotePolicyState.State1 =>
        e match {
          case ReadVote(x) => cloneNewState(d, VotePolicyState.State1)
          case Action2(x) => cloneNewState(d, VotePolicyState.State2)
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case VotePolicyState.State2 =>
        e match {
          case ReadVote(x) => cloneNewState(d, VotePolicyState.State2)
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case _ => throw new IllegalArgumentException("Illegal Operation");
    }
  }

}