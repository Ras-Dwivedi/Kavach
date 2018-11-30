package kavach.attestation
import java.io._

object WriteTestObject {
	// val filename = "statements"
	def write_object(ob: Derivation, oos: ObjectOutputStream):Unit= {
		oos.writeObject(ob)	
		println("wrote: "+ ob)
	}

	def write_object(obList: List[Derivation], filename: String):Unit= {
		// val oos = new ObjectOutputStream(new FileOutputStream(filename))
		var ob_List_initial = List[Derivation]()
		try{
			ob_List_initial = readObject.read_file(filename)
		}
		catch{
			case e: Exception => println(e)
				println(" error in reading the file")
		}
		val oos = new ObjectOutputStream(new FileOutputStream(filename))
		// try{
		// 	println("read the following \n\n\n" + ob_List_initial)
		// 	for(ob <- ob_List_initial){
		// 		write_object(ob, oos)
		// 	}
		// }	
		// catch{
		// 	case e: Exception => println(e)
		// 		println(" error in writing to file")
		// }	
		for(ob <- obList){
			write_object(ob, oos)
		}
		oos.close()
	}

	def write(filename: String) ={
		val a = Proposition("a")
		val b = Proposition("b")
		val c = Proposition("c")
		val d = Proposition("d")
		val impl = Implies(a, b)
		val ctx : Set[Expression] = Set(a)

		val deriv = Var(ctx, impl)
		val proof = App(deriv, impl)
		val P = Principal ("P")
		val Q = Principal ("P")
		val P_says = Says(P,c)
		val Q_says= Says(Q,d)

		// Only variable we are wriing are Derivation
		// println("from here \n\n\n\n\n\n\n")
		try{
		write_object(List(deriv, proof), filename)
		// write_object(proof)
		}
		catch{
			case e:Exception => println(e)
				println("returned from this file")
		}
		 // oos.close()
		// write_object(proof2)
	}// closing FileOutputStream
 }