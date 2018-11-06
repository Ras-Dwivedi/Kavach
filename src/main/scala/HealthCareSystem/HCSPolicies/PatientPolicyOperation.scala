/**
  * Represents the read dob operation
  * @param name
  */
case class ReadDOB(override val name: String) extends Operation(name)
/**
  * Represents the read gender operation
  * @param name
  */
case class ReadGender(override val name: String) extends Operation(name)
/**
  * Represents the annonimize patient operation
  * @param name
  */
case class AnonPatient(override val name: String) extends Operation(name)
