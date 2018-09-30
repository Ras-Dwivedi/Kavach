/**
  * Represents the read vote operation
  * @param name
  */
case class ReadVote(override val name: String) extends Operation(name)
/**
  * Represents the read credentials operation
  * @param name
  */
case class ReadCredential(override val name: String) extends Operation(name)
/**
  * Represents the annonimize vote operation
  * @param name
  */
case class AnonVote(override val name: String) extends Operation(name)