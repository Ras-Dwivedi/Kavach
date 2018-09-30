trait Event

case class ReadVote() extends Event
case class ReadCredential() extends Event
case class AnonVote() extends Event