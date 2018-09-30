class VotePolicy(var policyState: PolicyState) extends Policy(policyState) {

  private def changeState(s: PolicyState)= {
    policyState = s
  }

  override def transition(e: Operation) = {
    policyState match {
      case VotingState(_) =>
        e match {
          case ReadVote(_) => { /*no state change*/ }
          case ReadCredential(_) => { /*no state change*/ }
          case AnonVote(_) => changeState(new VotingCompletedState("Voting"))
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case VotingCompletedState(_) =>
        e match {
          case ReadVote(_) => { /*no state change*/ }
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case _ => throw new IllegalArgumentException("Illegal Operation");
    }
  }

}