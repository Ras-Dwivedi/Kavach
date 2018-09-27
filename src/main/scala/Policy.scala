object PolicyState extends Enumeration{
  type State = Value
  val State1, State2 = Value
}
case class Policy(name: String, policyState: PolicyState.State) // extends FsmState(state =  babyState)

trait Event

case class ReadVote(id: String) extends Event
case class Action2(id: String) extends Event

object Policy{

  private def cloneNewState(d: Policy, s: PolicyState.State) =
    d.copy(policyState = s)

  def transition(d: Policy, e: Event): Policy = {
    d.policyState match {
      case PolicyState.State1 =>
        e match {
          case ReadVote(x) => cloneNewState(d, PolicyState.State1)
          case Action2(x) => cloneNewState(d, PolicyState.State2)
          case _ => d
        }
      case PolicyState.State2 =>
        e match {
          case ReadVote(x) => cloneNewState(d, PolicyState.State2)
          case _ => d
        }
      case _ => d
    }
  }

}

object Tester {
  def main(args: Array[String]) {
    val test = new Policy("test", PolicyState.State1);
    println("Original Policy")
    println(test)
    println("Policy After transition")
    println(Policy.transition(test, Action2("hello")))

  }
}
