package kavach.attestation
import java.io._
import scala.util.control.Breaks._
object readObject {
	// val filename = "statements"
	var list= List[Derivation]()
	def read_Object(ois: ObjectInputStream): Derivation= {
		var ob = ois.readObject.asInstanceOf[Derivation]
		return ob
	}
	def read_file(filename: String): List[Derivation]= {
		val ois = new ObjectInputStream(new FileInputStream(filename))
		breakable{
			while (true) {
			 try {
		      		var ob=read_Object(ois)
		      		list= list :::List(ob)
		   	 }
		   	 catch{
		   	 case e: Exception => print(e)
		   	 		      println(" \nreached the end of the file")
		   	 		      break
		   	 }
			}
		}
		ois.close()
		return list
	}	
}