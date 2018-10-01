/**
  * Represents the add vote or cast vote operation
  * @param name
  */
case class CastVote(override val name: String) extends Operation(name)
/**
  * Represents the removing duplicate votes operation
  * @param name
  */
case class RemoveDuplicate(override val name: String) extends Operation(name)
/**
  * Represents the annonimize the votes in ballot box operation
  * @param name
  */
case class AnonimizeVote(override val name: String) extends Operation(name)

/**
  * Represents the counting of votes operation
  * @param name
  */
case class CountVotes(override val name: String) extends Operation(name)

/**
  * Represents the check vote operation from the voter
  * @param name
  */
case class CheckVote(override val name: String) extends Operation(name)