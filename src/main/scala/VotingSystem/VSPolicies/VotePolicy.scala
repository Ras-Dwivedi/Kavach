class VotePolicy(var policyState: PolicyState) extends Policy(policyState) {

  private def changeState(s: PolicyState)= {
    policyState = s
  }

  override def transition(e: Operation) = {
    policyState match {
      case V_VotingState(_) =>
        e match {
          case ReadVote(_) => { /*no state change*/ }
          case ReadCredential(_) => { /*no state change*/ }
          case ReadVoteId(_) => { /*no state change*/ }
          case AnonVote(_) => changeState(V_VotingCompletedState("Voting"))
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case V_VotingCompletedState(_) =>
        e match {
          case ReadVote(_) => { /*no state change*/ }
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case _ => throw new IllegalArgumentException("Illegal Operation");
    }
  }

}