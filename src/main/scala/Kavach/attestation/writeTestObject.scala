package kavach.attestation
import java.io._

object WriteTestObject(filname: String) {
	val oos = new ObjectOutputStream(new FileOutputStream(filname))
	def write_object(ob: Derivation):Unit= {
		oos.writeObject(ob)	
		println("wrote: "+ ob)
	}

	def write_object(obList: List[Derivation]):Unit= {	
		for(ob <- obList){
			write_object(ob, filname)
		}
	}

	def write() ={
		val a = Proposition("a")
		val b = Proposition("b")
		val impl = Implies(a, b)
		val ctx : Set[Expression] = Set(a)

		val deriv = Var(ctx, impl)
		val proof = App(deriv, impl)
		val P = Principal ("P")
		val Q = Principal ("P")
		// Only variable we are wriing are Derivation
		// println("from here \n\n\n\n\n\n\n")
		try{
		write_object(deriv, filname)
		write_object(proof, filname)
		}
		catch{
			case e:Exception => println(e)
				println("returned from this file")
		}
		 oos.close()
		// write_object(proof2)
	}// closing FileOutputStream
 }