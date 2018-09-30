trait Event

case class ReadVote(id: String) extends Event
case class Action2(id: String) extends Event