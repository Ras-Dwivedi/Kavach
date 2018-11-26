package kavach.attestation
//import scala.io.Source
import java.io._

object test {
	val oos = new ObjectOutputStream(new FileOutputStream("statement"))
	val ois = new ObjectInputStream(new FileInputStream("statement"))
	def write_object(ob: Expression) {
		oos.writeObject(ob)	
	}

	def read_Object: Expression {
		val ob = ois.readObject.asInstanceOf[Expression]
	}

    	def main(args: Array[String]) {
		val a = Proposition("a")
		val b = Proposition("b")
		val impl = Implies(a, b)
		val ctx : Set[Expression] = Set(a)

		val deriv = Var(ctx, impl)
		val proof = App(deriv, impl)
		val proof2 = App(Var(ctx, impl), impl)
		write_object(a)
		write_object(b)
		write_object(impl)
		write_object(deriv)
		write_object(proof)
		write_objec(proof2)
		while (true) {
		 try {
	      		var ob=ois.readObject().asInstanceOf[Expression]
	      		print(ob)
	   	 }
	   	 catch{
	   	 case e: Exception => print(e)
	   	 		      print("reached the end of the file")
	   	 		      break
	   	 }
		}
		oos.close()
		ois.close()

	//        println(proof.toString())
	//        println(proof2.toString())
	//        
		print("\n\n\n\n\n")
		print(impl)
		// println("hello, world!")
	}
}
