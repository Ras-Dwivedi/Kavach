class BallotBoxPolicy(var policyState: PolicyState) extends Policy(policyState) {

  private def changeState(s: PolicyState)= {
    policyState = s
  }

  override def transition(e: Operation) = {
    policyState match {
      case BB_VotingState(_) =>
        e match {
          case CastVote(_) => { /*no state change*/ }
          case CheckVote(_) => { /*no state change*/ }
          case RemoveDuplicate(_) => changeState(BB_AnnonimizeBallotBoxState("Annon Ballot Box"))
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case BB_AnnonimizeBallotBoxState(_) =>
        e match {
          case AnonimizeVote(_) => changeState(BB_VotingCompletedState("Voting Completed"))
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case BB_VotingCompletedState(_) =>
        e match {
          case CountVotes(_) => { /*no state change*/ }
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case _ => throw new IllegalArgumentException("Illegal Operation");
    }
  }

}