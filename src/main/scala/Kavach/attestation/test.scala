package kavach.attestation
//import scala.io.Source
import java.io._
import scala.util.control.Breaks._

object test {
	// val oos = new ObjectOutputStream(new FileOutputStream("statement"))
	val filename = "statement"
	val oos = new ObjectOutputStream(new FileOutputStream(filename, true))
	// oos.close()
	val ois = new ObjectInputStream(new FileInputStream(filename))
	// def write_object(ob: Derivation)= {
	// 	oos.writeObject(ob)	
	// }

	def read_Object(): Derivation= {
		println("here")
		var ob1 = ois.readObject.asInstanceOf[Derivation]
		return ob1
	}

    	def main(args: Array[String]) {
		// val a = Proposition("a")
		// val b = Proposition("b")
		// val impl = Implies(a, b)
		// val ctx : Set[Expression] = Set(a)

		// val deriv = Var(ctx, impl)
		// val proof = App(deriv, impl)
		// val P = Principal (P)
		// val Q = Principal (P)
		// // Only variable we are wriing are Derivation
		// write_object(deriv)
		// write_object(proof)
		// write_object(proof2)
		WriteTestObject(filename).write()
		breakable{
			while (true) {
			 try {
		      		var ob=read_Object();
		      		println("ctx:	"+ob.ctx)
		      		println("statetemnt: 	"+ob.st)
		      		println(ob+" \n\n\n\n\n")
		   	 }
		   	 catch{
		   	 case e: Exception => print(e)
		   	 		      println("reached the end of the file")
		   	 		      break
		   	 }
			}
		}
		oos.close()
		ois.close()
       
		print("\n\n\n\n\n")
		// print(impl)
		// println("hello, world!")
	}
}
