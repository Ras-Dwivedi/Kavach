package kavach.attestation

import scala.io.Source


final case class InvalidDerivationException(
    private val message: String = "Invalid derivation",
    private val cause: Throwable = None.orNull)
  extends java.lang.RuntimeException(message, cause)

abstract class Expression

case object True extends Expression
case class Principal (v : String)
case class Proposition (v: String) extends Expression

case class Says (P: Principal, s: Expression) extends Expression {
    val v = "Says"
    val left: Principal = P
    val right: Expression = s
}
case class And(a: Expression, b: Expression) extends Expression {
    val v = "AND"
    val left: Expression = a
    val right: Expression = b
}

case class Or(a: Expression, b: Expression) extends Expression {
    val v = "OR"
    val left: Expression = a
    val right: Expression = b
}
case class Implies(a: Expression, b: Expression) extends Expression {
    val v = "IMPLIES"
    val left: Expression= a
    val right: Expression= b
}


//each case class of this class represent a one step Derivation 
abstract class Derivation { 
    val ctx: Set[Expression]
    val st: Expression
}

case class Var (ctx1: Set[Expression], s:Expression) extends Derivation {
    override val ctx = ctx1 + s
    override val st = s
}

case class Unit (ctx: Set[Expression]) extends Derivation{
    override val st = True
}

case class Lam(d: Derivation, s1: Expression, s2: Expression) extends Derivation{
    if(!d.ctx.contains(s1) || d.st != s2) {
        throw InvalidDerivationException()
    }
    override val ctx = d.ctx - s1
    override val st = Implies(s1,s2)
}

case class App(d: Derivation, s1:Implies) extends Derivation {
    if(!(d.st == s1 && d.ctx.contains(s1.left))) {
        throw InvalidDerivationException()
    }
    override val ctx = d.ctx
    override val st = s1.right
}

/*


case class Pair(d1: Derivation, d2: Derivation) extends Derivation{
    if (d1.ctx==d2.ctx) { 
        ctx = d1.ctx    
        st = AND(d1.st, d2.st)
    }
}


case class Proj1(d: Derivation, s: And) extends Derivation{
    if (d.st==s) {
        ctx= d.ctx
        st=s.left
    }
}

case class Proj2(d: Derivation, s: And) extends Derivation{
    if (d.st==s) {
        ctx= d.ctx
        st=s.right
    }
}

case class Inj1(d: Derivation, s: And) extends Derivation{
        ctx= d.ctx
        st=Or(d.st, s)
}


case class Inj2(d: Derivation, s: And) extends Derivation{
        ctx= d.ctx
        st=Or(s, d.st)
}


case class UnitM(d: Derivation, P: Principal) extends Derivation{
        ctx= d.ctx
        st=Says(P, d.st)
}

case class Case (d1: Derivation, d2: Derivation, d3: Derivation, s: Expression){
    assert (d1.context == d2.context - d1.st.left)
    assert (d1.context == d3.context - d1.st.right)
    assert(d1.st.v=="OR")
    assert(d2.st == d3.st)
    ctx = d1.ctx
    st = d2.xt
}

case class Bindm(d1: Derivation, d2: Derivation) extends Derivation{
    assert(d1.st.v=="Says")
    assert(d2.st.v=="Says")
    asser(d1.st.left == d2.st.left)
    assert(d1.ctx == d2.ctx - d1.st.right)
    ctx = d1.ctx
    st= d2.st
}

*/

// this does the reading part
object AST {
    def main(args: Array[String]) {
        val a = Proposition("a")
        val b = Proposition("b")
        val impl = Implies(a, b)
        val ctx : Set[Expression] = Set(a)

        val deriv = Var(ctx, impl)
        val proof = App(deriv, impl)

        println(proof.toString())
        println("hello, world!")
    }
}

