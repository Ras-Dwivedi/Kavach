import scala.io.Source

// this does the reading part
object AST {
	def read(filename: String){
		// val filename = "data.txt"
		for (line <- Source.fromFile(filename).getLines) {
			var text: Array[String] = line.split(" ")
			for(i <- text.toList){
				var j : Token = getToken(i)
				// println(j.v+ "     "+ j.token)
			}
		}
	}

	def main(args: Array[String]) {
		read("data.txt")
	}
}


abstract class Expression

case class Principal (v : String)
case class Proposition (v: String) extends Expression{
}

case class Says (P: Principal, s: Expression) extends Expression {
	val v = "Says"
	val left: Principal= P
	val right: Expression= s
}
case class And(a: Expression, b: Expression) extends Expression {
	val v = "AND"
	val left: Expression= a
	val right: Expression= b
}

case class Or(a: Expression, b: Expression) extends Expression {
 	val v = "OR"
	val left: Expression= a
	val right: Expression= b
}
case class Implies(a: Expression, b: Expression) extends Expression {
	val v = "IMPLIES"
	val left: Expression= a
	val right: Expression= b
}


//each case class of this class represent a one step derivation 
abstract class derivation { 
  val ctx: Set[Expression]
  val st: Expression
}

case class Var (ctx1: Set[Expression], s:Expression) extends derivation{
	override ctx = ctx1 + s
	override val st = s
}

case class Unit (ctx: Set[Expression]) extends derivation{
	st: Proposition("True")
}

case class Lam(d: derivation, s1: Expression, s2: Expression) extends derivation{
	if(d.ctx.contains(s1) && d.st = s2) {
		ctx = d.ctx -s1
		st = Implies(s1,s2)

	}

}

case class App(ctx: Set[Expression], s1:Implies, s2:Expression) extends derivation {
	if(ctx.contains(s1.right) && ctx.contains(s2))  val st = s1.right
}



case class Pair(d1: derivation, d2: derivation) extends derivation{
	if (d1.ctx==d2.ctx) { 
		ctx = d1.ctx	
		st = AND(d1.st, d2.st)
}


case class Proj1(d: derivation, s: And) extends derivation{
	if (d.st==s) {
		ctx= d.ctx
		st=s.left
	}
}

case class Proj2(d: derivation, s: And) extends derivation{
	if (d.st==s) {
		ctx= d.ctx
		st=s.right
	}
}

case class Inj1(d: derivation, s: And) extends derivation{
		ctx= d.ctx
		st=Or(d.st, s)
}


case class Inj2(d: derivation, s: And) extends derivation{
		ctx= d.ctx
		st=Or(s, d.st)
}


case class UnitM(d: derivation, P: Principal) extends derivation{
		ctx= d.ctx
		st=Says(P, d.st)
}

case class Case (d1: derivation, d2: derivation, d3: derivation, s: Expression){
	assert (d1.context == d2.context - d1.st.left)
	assert (d1.context == d3.context - d1.st.right)
	assert(d1.st.v=="OR")
	assert(d2.st == d3.st)
	ctx = d1.ctx
	st = d2.xt
}

case class Bindm(d1: derivation, d2: derivation) extends derivation{
	assert(d1.st.v=="Says")
	assert(d2.st.v=="Says")
	asser(d1.st.left == d2.st.left)
	assert(d1.ctx == d2.ctx - d1.st.right)
	ctx = d1.ctx
	st= d2.st
}

