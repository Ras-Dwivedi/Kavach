package kavach.attestation
//import scala.io.Source
import java.io._
import scala.util.control.Breaks._

object test {
	val filename = "statements"

    	def main(args: Array[String]) {
		WriteTestObject.write(filename)
		println(" wrting process completed, now reading \n")
		val list = readObject.read_file(filename)
		for(x <- list){
			println(x)
			println("context:	"+x.ctx)
			println("statement:	"+x.st)
			println("\n")
		}
		println("reading done. closing the streams")
	
	}
}
