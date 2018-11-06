class PatientPolicy(var policyState: PolicyState) extends Policy(policyState) {

  private def changeState(s: PolicyState)= {
    policyState = s
  }


  override def transition(e: Operation) = {
    policyState match {
      case PatientInitial(_) =>
        e match {
          case ReadDOB(_) => { /*no state change*/ }
          case ReadGender(_) => { /*no state change*/ }
          case AnonPatient(_) => changeState(PatientAnon("Voting"))
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case PatientAnon(_) =>
        e match {
          case ReadGender(_) => { /*no state change*/ }
          case _ => throw new IllegalArgumentException("Illegal Operation");
        }
      case _ => throw new IllegalArgumentException("Illegal Operation");
    }
  }

}