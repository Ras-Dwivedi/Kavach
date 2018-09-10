package votes
class Vote(var cred:Int, var id:Int, var vote:String) {

  override def toString = s"$cred $id $vote"
}
