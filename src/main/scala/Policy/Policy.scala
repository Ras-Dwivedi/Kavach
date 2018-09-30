/**
  * Represents the Policy associated with a Kavach Object
  * @param state
  */
abstract class Policy (state: PolicyState){
  /**
    * Represents the transition fucntion that changes the state of the policy on performing the op Operation
    */
  def transition(op: Operation) : Unit
}