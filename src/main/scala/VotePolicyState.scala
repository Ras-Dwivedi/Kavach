object VotePolicyState extends PolicyState{
  type State = Value
  val Voting, VotingComplete = Value
}